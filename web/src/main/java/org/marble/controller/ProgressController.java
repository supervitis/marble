package org.marble.controller;

import java.io.Serializable;
import java.util.HashMap;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import org.marble.service.ProgressWall;

@ManagedBean
@SessionScoped
public class ProgressController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            name;

    private String            value;

    @Inject
    ProgressWall              progressWall;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSayWelcome() {
        // check if null?
        if ("".equals(name) || name == null) {
            return "";
        } else {
            this.value = "Value message : Welcome " + progressWall.getProgressMap().toString();
            return "Ajax message : Welcome " + name;
        }
    }

    public HashMap<String, String> getProgressMap() {
        return progressWall.getProgressMap();
    }
}
