package org.marble.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.marble.model.GlobalConfiguration;
import org.marble.service.GlobalConfigurationRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
@Model
public class GlobalConfigurationController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private GlobalConfigurationRegistration globalConfigurationRegistration;

    private GlobalConfiguration newGlobalConfiguration;

    @Produces
    @Named
    public GlobalConfiguration getNewGlobalConfiguration() {
        return newGlobalConfiguration;
    }

    public void register() throws Exception {
        try {
            globalConfigurationRegistration.register(newGlobalConfiguration);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Updated!", "Modification Successful"));
            initNewGlobalConfiguration();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Modification Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }
    
    public void delete() throws Exception {
        try {
            globalConfigurationRegistration.delete(newGlobalConfiguration);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted!", "Deletion Successful"));
            initNewGlobalConfiguration();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Deletion Unsuccessful");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    public void initNewGlobalConfiguration() {
        newGlobalConfiguration = new GlobalConfiguration();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Global configuration operation failed. See server log for more information";
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
