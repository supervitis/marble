package org.marble.service.processors;

import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.marble.util.Constants;

@RequestScoped
public class BasicProcessor {

    @Inject
    private PolarityCalculator calculator;
    private Boolean            ignoreNeutralSentences = Boolean.FALSE;

    @Inject
    private Logger log;

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
                results = this.calculator.calculatePolarity(phrase);
                log.trace("Trying with four words: " + phrase);
            }
            if (results == null && (i + 2 < words.length)) {
                j--;
                phrase = words[i] + " " + words[i + 1] + " " + words[i + 2];
                results = this.calculator.calculatePolarity(phrase);
                log.trace("Trying with three words: " + phrase);
            }
            if (results == null && (i + 1 < words.length)) {
                j--;
                phrase = words[i] + " " + words[i + 1];
                results = this.calculator.calculatePolarity(phrase);
                log.trace("Trying with two words: " + phrase);
            }
            if (results == null) {
                j--;
                phrase = words[i];
                results = this.calculator.calculatePolarity(phrase);
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
