package org.marble.model;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class FlotChart {
    private Map<String, Object> mainOptions;
    private Map<String, Object> overviewOptions;
    private List<Map<String, Object>> data;

    public Map<String, Object> getMainOptions() {
        return mainOptions;
    }

    public void setMainOptions(Map<String, Object> mainOptions) {
        this.mainOptions = mainOptions;
    }

    public Map<String, Object> getOverviewOptions() {
        return overviewOptions;
    }

    public void setOverviewOptions(Map<String, Object> overviewOptions) {
        this.overviewOptions = overviewOptions;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
