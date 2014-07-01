package org.marble.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.Topic;
import org.marble.service.TopicRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class TopicController {

    @Inject
    private FacesContext      facesContext;

    @Inject
    private TopicRegistration topicRegistration;

    private Topic             newTopic;
    
    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    @Produces
    @Named
    public Topic getNewTopic() {
        return newTopic;
    }

    public void register() throws Exception {
        try {
            topicRegistration.register(newTopic);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!",
                    "Modification Successful"));
            initNewTopic();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
                    "Modification Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public void update() throws Exception {
        try {
            topicRegistration.update(newTopic);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!",
                    "Modification Successful"));
            initNewTopic();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
                    "Modification Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public void delete() throws Exception {
        try {
            topicRegistration.delete(newTopic);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!",
                    "Deletion Successful"));
            initNewTopic();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
                    "Deletion Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    public void deleteStatuses() throws Exception {
        try {
            topicRegistration.deleteStatuses(newTopic);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Data Deleted.", "Deletion Successful"));
            initNewTopic();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
                    "Deletion Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void refreshMongoStatus(Topic topic) {
        try {
            topicRegistration.refreshMongoStatus(topic);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mongo Status Refreshed!",
                    "Mongo Status Refreshed"));
            initNewTopic();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
                    "Mongo Status refreshed");
            facesContext.addMessage(null, m);
        }
    }
    

    @PostConstruct
    public void initNewTopic() {
        newTopic = new Topic();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Topic operation failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
}
