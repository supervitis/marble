package org.marble.controller;

import java.io.File;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.ExecutionCommands;
import org.marble.model.Topic;
import org.marble.service.ProgressWall;
import org.marble.service.extractors.TwitterExtractor;
import org.marble.util.Constants;

@Model
public class ExtractorController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Logger log;

    @Inject
    private TwitterExtractor twitterExtractor;

    @Inject
    private ProgressWall progressWall;

    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void extract(Topic topic) {
        twitterExtractor.searchAndLoadStatuses(topic);
        FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Extraction of topic <" + topic.getName() + "> was started.",
                "Processed");
        facesContext.addMessage(null, m);
    }

    public void requestStop(Topic topic) {
        progressWall.pushExtractorMessage(topic.getName(),
                ExecutionCommands.STOP);
        FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Request to stop extraction of topic <" + topic.getName()
                        + "> was sent.", "Request sent");
        facesContext.addMessage(null, m);
    }

    public void insertDataFromFile(Topic topic) throws Exception {
        try {
            // topicRegistration.update(newTopic);
            log.info("Reading file: " + Constants.MONGO_DATAFILE_LOCATION + "/"
                    + this.filename + " for topic <" + topic.getName() + ">");
            File file = new File(Constants.MONGO_DATAFILE_LOCATION + "/"
                    + this.filename);

            if (file.exists()) {
                mongoDatastoreOperations.setCollection(topic.getName());
                mongoDatastoreOperations.insertDataFromFile(topic, file);
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Data Uploaded.",
                        "Data Uploaded."));
            } else {
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Data file not found.",
                        "Data file not found."));
            }

        } catch (Exception e) {
            log.error("An error ocurred while inserting data from file.", e);
            FacesMessage m = new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    "Some errors were found while inserting the data. Check the logs of the application.",
                    "Error");
            facesContext.addMessage(null, m);
        }
    }
}