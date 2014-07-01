package org.marble.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.marble.model.Topic;
import org.marble.service.processors.Controller;

@ManagedBean
@Named
public class ProcessorController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private Logger       log;

    @Inject
    private Controller   controller;
    
    public void process(Topic topic) {
        controller.process(topic);
        FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Topic with keyword <" + topic.getKeywords() + "> was processed.", "Processed");
        facesContext.addMessage(null, m);
    }
}