package org.marble.service.statistics.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class TopicGlobalAnatomy {
    private Integer statuses;
    private Integer originals;
    private Integer retweets;
    private Integer totalUniqueUsers;
    private Map<String, Integer> uniqueUsers;
    private Integer totalTimeZones;
    private Map<String, Integer> timeZones;

    public TopicGlobalAnatomy() {
        this.statuses = 0;
        this.originals = 0;
        this.retweets = 0;
        this.totalTimeZones = 0;
        this.uniqueUsers = new HashMap<String, Integer>();
        this.totalUniqueUsers = 0;
        this.timeZones = new HashMap<String, Integer>();
    }

    public Integer getStatuses() {
        return statuses;
    }

    public void setStatuses(Integer statuses) {
        this.statuses = statuses;
    }

    public void incrementStatuses() {
        this.statuses++;
    }

    public Integer getOriginals() {
        return originals;
    }

    public void setOriginals(Integer originals) {
        this.originals = originals;
    }

    public void incrementOriginals() {
        this.originals++;
    }

    public Integer getRetweets() {
        return retweets;
    }

    public void setRetweets(Integer retweets) {
        this.retweets = retweets;
    }

    public void incrementRetweets() {
        this.retweets++;
    }

    public Integer getTotalUniqueUsers() {
        return totalUniqueUsers;
    }

    public void setTotalUniqueUsers(Integer totalUniqueUsers) {
        this.totalUniqueUsers = totalUniqueUsers;
    }

    public void incrementTotalUniqueUsers() {
        this.totalUniqueUsers++;
    }

    public Map<String, Integer> getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(Map<String, Integer> uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public void addUser(String user) {
        if (this.uniqueUsers.get(user) != null) {
            this.uniqueUsers.put(user, this.uniqueUsers.get(user) + 1);
        }
        else {
            this.uniqueUsers.put(user, 1);
        }
    }

    public Integer getTotalTimeZones() {
        return totalTimeZones;
    }

    public void setTotalTimeZones(Integer totalTimeZones) {
        this.totalTimeZones = totalTimeZones;
    }

    public void incrementTotalTimeZones() {
        this.totalTimeZones++;
    }

    public Map<String, Integer> getTimeZones() {
        return timeZones;
    }

    public void setTimeZones(Map<String, Integer> timeZones) {
        this.timeZones = timeZones;
    }

    public void addTimeZone(String timeZone) {
        if (this.timeZones.get(timeZone) != null) {
            this.timeZones.put(timeZone, this.timeZones.get(timeZone) + 1);
        }
        else {
            this.timeZones.put(timeZone, 1);
        }
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void sortAndTotalDetails() {
        this.uniqueUsers = sortDetailsMap(this.uniqueUsers);
        this.totalUniqueUsers = this.uniqueUsers.size();
        this.timeZones = sortDetailsMap(this.timeZones);
        this.totalTimeZones = this.timeZones.size();
    }

    public void trimUsers(Integer count) {
        this.uniqueUsers = trimDetailsMap(this.uniqueUsers, count);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<String, Integer> sortDetailsMap(Map<String, Integer> unsortMap) {

        List<Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // put sorted list into map again
        // LinkedHashMap make sure order in which keys were inserted
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static Map<String, Integer> trimDetailsMap(Map<String, Integer> untrimmedMap, int count) {
        Map<String, Integer> trimmedMap = new LinkedHashMap<String, Integer>();

        if (count < untrimmedMap.size()) {
            Integer i = 0;
            for (String key : untrimmedMap.keySet()) {
                if (!(i < count)) {
                    break;
                }
                trimmedMap.put(key, untrimmedMap.get(key));
                i++;
            }
        }

        return trimmedMap;
    }
}
