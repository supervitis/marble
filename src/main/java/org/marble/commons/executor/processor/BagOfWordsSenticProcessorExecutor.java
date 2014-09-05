package org.marble.commons.executor.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.ProcessedStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.model.Constants;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.SenticNetService;
import org.marble.commons.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BagOfWordsSenticProcessorExecutor implements ProcessorExecutor {

    private static final Logger log = LoggerFactory.getLogger(BagOfWordsSenticProcessorExecutor.class);

    @Autowired
    ExecutionService executionService;

    @Autowired
    TopicService topicService;

    @Autowired
    DatastoreService datastoreService;

    @Autowired
    SenticNetService senticNetService;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Execution execution;

    // Custom parameters
    private Boolean ignoreNeutralSentences = Boolean.FALSE;

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @Override
    public void run() {
        String msg = "";
        try {
            log.info("Initializing execution...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {

            Integer id = execution.getId();

            msg = "Starting Bag of Words Sentic Processor <" + id + ">.";
            log.info(msg);
            execution.appendLog(msg);

            // Changing execution state
            execution.setStatus(ExecutionStatus.Running);
            execution = executionService.save(execution);

            // Get the associated topic
            Topic topic = topicService.getTopic(execution.getTopic().getId());

            // Drop current processed statuses
            datastoreService.findAllAndRemoveByTopicId(topic.getId(), ProcessedStatus.class);

            // TODO Include ignore neutral sentences from global configuration
            this.ignoreNeutralSentences = Boolean.FALSE;
            log.info("Getting statuses for topic <" + topic.getId() + ">.");
            List<OriginalStatus> statuses = datastoreService.findByTopicId(topic.getId(), OriginalStatus.class);

            log.info("Statuses count is <" + statuses.size() + ">");
            Integer count = 0;
            for (OriginalStatus status : statuses) {
                log.debug("Status text is <" + status.getText() + ">");
                if (status.getCreatedAt() == null) {
                    continue;
                }
                String text = status.getText();
                if (text == null) {
                    log.debug("Status text for id <" + status.getId() + "> is null. Skipping...");
                    continue;
                }
                log.debug("Analysing text: " + text.replaceAll("\n", ""));

                Float polarity = this.processStatus(text);

                log.debug("Polarity for text <" + text.replaceAll("\n", "") + "> is <" + polarity + ">");

                ProcessedStatus processedStatus = new ProcessedStatus(status);
                processedStatus.setPolarity(polarity);

                datastoreService.save(processedStatus);

                count++;
                
                if ((count % 100) == 0) {
                    msg = "Statuses processed so far: <" + count + ">";
                    log.info(msg);
                    execution.appendLog(msg);
                    executionService.save(execution);
                }
            }

            log.info("The bag of words sentic processor operation for topic <" + topic.getName() + "> has finished.");

            msg = "Total of statuses processed: <" + count + ">";
            log.info(msg);
            execution.appendLog(msg);
            
            msg = "The Bag of Words Sentic Processor execution for this topic has finished.";
            log.info(msg);
            execution.appendLog(msg);
            execution.setStatus(ExecutionStatus.Stopped);
            
            execution = executionService.save(execution);
        } catch (Exception e) {
            msg = "An error ocurred while running execution <" + execution.getId() + ">. Execution aborted.";
            log.error(msg, e);
            execution.appendLog(msg);
            execution.setStatus(ExecutionStatus.Aborted);
            try {
                execution = executionService.save(execution);
            } catch (InvalidExecutionException e1) {
                log.error("Status couldn't be refreshed on the execution object.");
            }

            return;
        }
    }

    public Boolean getIgnoreNeutralSentences() {
        return ignoreNeutralSentences;
    }

    public void setIgnoreNeutralSentences(Boolean ignoreNeutralSentences) {
        this.ignoreNeutralSentences = ignoreNeutralSentences;
    }

    public Float calculateSentencePolarity(String words[]) {

        Float polarity = 0f;

        Integer j = -1;
        for (Integer i = 0; i < words.length; i++) {
            if (i <= j) {
                continue;
            }
            j = Math.min(i + 3, words.length);
            Float results = null;
            String phrase = "";

            if (i + 3 < words.length) {
                phrase = words[i] + " " + words[i + 1] + " " + words[i + 2] + " " + words[i + 3];
                results = senticNetService.getPolarity(phrase);
                log.trace("Trying with four words: " + phrase);
            }
            if (results == null && (i + 2 < words.length)) {
                j--;
                phrase = words[i] + " " + words[i + 1] + " " + words[i + 2];
                results = senticNetService.getPolarity(phrase);
                log.trace("Trying with three words: " + phrase);
            }
            if (results == null && (i + 1 < words.length)) {
                j--;
                phrase = words[i] + " " + words[i + 1];
                results = senticNetService.getPolarity(phrase);
                log.trace("Trying with two words: " + phrase);
            }
            if (results == null) {
                j--;
                phrase = words[i];
                results = senticNetService.getPolarity(phrase);
                log.trace("Trying with one word: " + phrase);
            }

            if (results != null) {
                polarity += results;
                log.trace("Result for this group: " + results);
            } else {
                log.trace("No results found for this group.");
            }
        }

        return polarity;

    }

    public float processStatus(String originalText) {

        String processedText = originalText;
        log.trace("Original text to process: " + originalText);
        // Clean up symbols
        processedText = processedText.replaceAll(Constants.URL_PATTERN, "_URL_");
        // Replace "separator symbols
        processedText = processedText.replace("\n", " ").replace("\r", "");
        processedText = processedText.replaceAll("([\\(\\)\"-])", " ");
        processedText = processedText.replaceAll("\\.+", ".");
        processedText = processedText.replaceAll("[ \t]+", " ");
        // lowercase everything
        processedText = processedText.toLowerCase();
        // Split into sentences
        log.trace("Cleaned-up text: " + processedText);

        String[] sentences = processedText.split("[\\.,;!?]");

        log.trace("Splitted text: " + Arrays.toString(sentences));

        Float polarity = 0f;
        Integer count = 0;
        for (String sentence : sentences) {
            // Split sentences into words
            // TODO Do this using ntlk alternative (PoS tool)
            String words[] = sentence.trim().split(" ");
            Float subpolarity = this.calculateSentencePolarity(words);
            if (subpolarity != 0) {
                count++;
            }
            polarity += subpolarity;
            log.debug("Result for sentence <" + sentence + "> :" + subpolarity + ";");
        }
        if (this.ignoreNeutralSentences) {
            return polarity / count;
        } else {
            return polarity / sentences.length;
        }
    }
}