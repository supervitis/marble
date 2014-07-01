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

public class IndividualDetail {
    private Integer statuses;
    private Integer originals;
    private Integer retweets;
    private Integer totalUniqueUsers;
    private Map<String, Integer> uniqueUsers;
    private Integer totalTimeZones;
    private Map<String, Integer> timeZones;
    private Integer positiveStatuses;
    private Integer negativeStatuses;
    private Integer positiveMinusNegative;
    private Double averagePolarity;

    public IndividualDetail() {
        this.statuses = 0;
        this.originals = 0;
        this.retweets = 0;
        this.totalUniqueUsers = 0;
        this.uniqueUsers = new HashMap<String, Integer>();
        this.totalTimeZones = 0;
        this.timeZones = new HashMap<String, Integer>();
        this.positiveStatuses = 0;
        this.negativeStatuses = 0;
        this.positiveMinusNegative = 0;
        this.averagePolarity = 0D;
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

    public Integer getPositiveStatuses() {
        return positiveStatuses;
    }

    public void setPositiveStatuses(Integer positiveStatuses) {
        this.positiveStatuses = positiveStatuses;
    }

    public void incrementPositiveStatuses() {
        this.positiveStatuses++;
        this.positiveMinusNegative++;
    }

    public Integer getNegativeStatuses() {
        return negativeStatuses;
    }

    public void setNegativeStatuses(Integer negativeStatuses) {
        this.negativeStatuses = negativeStatuses;
    }

    public void incrementNegativeStatuses() {
        this.negativeStatuses++;
        this.positiveMinusNegative--;
    }

    public Integer getPositiveMinusNegative() {
        return positiveMinusNegative;
    }

    public void setPositiveMinusNegative(Integer positiveMinusNegative) {
        this.positiveMinusNegative = positiveMinusNegative;
    }

    public Double getAveragePolarity() {
        return averagePolarity;
    }

    public void setAveragePolarity(Double averagePolarity) {
        this.averagePolarity = averagePolarity;
    }

    public void addToAveragePolarity(Double amount) {
        this.averagePolarity += amount;
    }

    public void sortAndTotal() {
        this.averagePolarity = this.averagePolarity/this.statuses;
        this.totalUniqueUsers = this.uniqueUsers.size();
        //this.uniqueUsers = sortDetailsMap(this.uniqueUsers);
        this.uniqueUsers = null;
        this.totalTimeZones = this.timeZones.size();
        //this.timeZones = sortDetailsMap(this.timeZones);
        this.timeZones = null;
        
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
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
    
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
