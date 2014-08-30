package org.marble.commons.dao.model;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import twitter4j.Status;

@Document(collection = "SurveyInfo")
public class SurveyInfo {

    private Map<String, String> questionsAndAnswers;

    public SurveyInfo() {
      this.questionsAndAnswers = new HashMap<String, String>();
    }

    public SurveyInfo(Map<String, String> questionsAndAnswers) {
      this.questionsAndAnswers = questionsAndAnswers;
    }

    public Map<String, String> getQuestionsAndAnswers() {
      return questionsAndAnswers;
    }

    public void setQuestionsAndAnswers(Map<String, String> questionsAndAnswers) {
      this.questionsAndAnswers = questionsAndAnswers;
    }

    public SurveyInfo addQuestionAndAnswer(String question, String answer) {
      this.questionsAndAnswers.put(question, answer);
      return this;
    }

  }  