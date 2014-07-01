package org.marble.model;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class MorrisChart {
    private String element;
    private List<Map<String, String>> data;
    private String xkey;
    private List<String> ykeys;
    private List<String> labels;
    private Integer pointSize = 2;
    private String hideHover = "auto";
    private Boolean resize = Boolean.TRUE;
    private Boolean smooth = Boolean.FALSE;
    private double fillOpacity = 0.5;
    public String getElement() {
        return element;
    }
    public void setElement(String element) {
        this.element = element;
    }
    public List<Map<String, String>> getData() {
        return data;
    }
    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
    public String getXkey() {
        return xkey;
    }
    public void setXkey(String xkey) {
        this.xkey = xkey;
    }
    public List<String> getYkeys() {
        return ykeys;
    }
    public void setYkeys(List<String> ykeys) {
        this.ykeys = ykeys;
    }
    public List<String> getLabels() {
        return labels;
    }
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    public Integer getPointSize() {
        return pointSize;
    }
    public void setPointSize(Integer pointSize) {
        this.pointSize = pointSize;
    }
    public String getHideHover() {
        return hideHover;
    }
    public void setHideHover(String hideHover) {
        this.hideHover = hideHover;
    }
    public Boolean getResize() {
        return resize;
    }
    public void setResize(Boolean resize) {
        this.resize = resize;
    }
    
    public Boolean getSmooth() {
        return smooth;
    }
    public void setSmooth(Boolean smooth) {
        this.smooth = smooth;
    }
    public double getFillOpacity() {
        return fillOpacity;
    }
    public void setFillOpacity(double fillOpacity) {
        this.fillOpacity = fillOpacity;
    }
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

     
}
