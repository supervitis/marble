package org.marble.commons.executor.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.ProcessedStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.executor.plotter.OriginalStatusesPlotterExecutor;
import org.marble.commons.model.Constants;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.DatastoreService;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.SenticNetService;
import org.marble.commons.service.TopicService;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BagOfWordsSenticProcessorExecutor implements ProcessorExecutor {

    private static final Logger log = LoggerFactory.getLogger(BagOfWordsSenticProcessorExecutor.class);

    public static final Map<String, String> availableOperations;

    public static final Map<String, String> availableParameters;

    static {
        Map<String, String> operations = new TreeMap<>();
        operations.put("basicProcessor", "Basic Processor.");
        availableOperations = Collections.unmodifiableMap(operations);
    }
    
    static {
        Map<String, String> parameters = new TreeMap<>();
        parameters.put("Param1", "Param 1.");
        parameters.put("Param2", "Param 2.");
        availableParameters = Collections.unmodifiableMap(parameters);
    }

    private enum OperationType
    {
        REGULAR
    }
    
    @Autowired
    ExecutionService executionService;

    @Autowired
    TopicService topicService;

    @Autowired
    DatastoreService datastoreService;

    @Autowired
    SenticNetService senticNetService;

    private Execution execution;
    
    private Map<String, String> parameters;

    // Custom parameters
    private Boolean ignoreNeutralSentences = Boolean.FALSE;

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }
    
    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;

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

            msg = "Starting Bag of Words Sentic processor <" + id + ">.";
            log.info(msg);
            execution.appendLog(msg);

            // Changing execution state
            execution.setStatus(ExecutionStatus.Running);
            execution = executionService.save(execution);
            
            switch (this.execution.getModuleParameters().getOperation()) {
            case "plotAllStatuses":
                process(OperationType.REGULAR);
                break;
            }
            
        } catch (Exception e) {
            msg = "An error ocurred while processing statuses with execution <" + execution.getId() + ">. Execution aborted.";
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

    public void process(OperationType operationType) throws InvalidExecutionException {
        
        String msg = "";
        // Get the associated topic
        Topic topic = execution.getTopic();

        // Drop current processed statuses
        datastoreService.findAllAndRemoveByTopicId(topic.getId(), ProcessedStatus.class);

        // TODO Include ignore neutral sentences from global configuration
        this.ignoreNeutralSentences = Boolean.FALSE;
        log.info("Getting statuses for topic <" + topic.getId() + ">.");

        log.info("There are <" + datastoreService.countAll(OriginalStatus.class) + "> statuses to process.");

        // List<OriginalStatus> statuses =
        // datastoreService.findByTopicId(topic.getId(),
        // OriginalStatus.class);
        DBCursor statusesCursor = datastoreService.findCursorByTopicId(topic.getId(), OriginalStatus.class);

        log.info("There are <" + statusesCursor.count() + "> statuses to process.");
        Integer count = 0;
        while (statusesCursor.hasNext()) {
        //for (OriginalStatus status : statuses) {
            DBObject rawStatus = statusesCursor.next();
            OriginalStatus status = datastoreService.getConverter().read(OriginalStatus.class, rawStatus);
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