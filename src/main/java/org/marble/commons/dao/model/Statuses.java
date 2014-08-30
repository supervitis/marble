package org.marble.commons.dao.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.core.mapping.Document;

import twitter4j.Status;

@Document(collection = "statuses")
public class Statuses {

    private String ojete;
    private List<Status> statuses;

    public Statuses() {
      this.statuses = new ArrayList<Status>();
    }

    public Statuses(List<Status> statuses) {
      this.statuses = statuses;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }


    public Statuses addStatus(Status status) {
      this.statuses.add(status);
      return this;
    }

    public String getOjete() {
        return ojete;
    }

    public void setOjete(String ojete) {
        this.ojete = ojete;
    }
  }  