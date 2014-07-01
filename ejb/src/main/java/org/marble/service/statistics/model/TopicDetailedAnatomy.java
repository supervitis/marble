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

public class TopicDetailedAnatomy {
    private Map<Long, IndividualDetail> data;

    public TopicDetailedAnatomy() {
        this.setData(new HashMap<Long, IndividualDetail>());
    }

    public Map<Long, IndividualDetail> getData() {
        return data;
    }

    public void setData(Map<Long, IndividualDetail> data) {
        this.data = data;
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public void sortAndTotalDetails(Integer stepSize) {
        // We need to modify the timestamp Slot to reflect the actual date
        Map<Long, IndividualDetail> timedData = new HashMap<Long, IndividualDetail>();
        for (Entry<Long, IndividualDetail> entry : data.entrySet()) {
            entry.getValue().sortAndTotal();
            timedData.put(entry.getKey() * stepSize, entry.getValue());
        }

        this.data = sortDataMap(timedData);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Map<Long, IndividualDetail> sortDataMap(Map<Long, IndividualDetail> timedData) {

        List<Entry<Long, IndividualDetail>> list = new LinkedList<Map.Entry<Long,
                IndividualDetail>>(timedData.entrySet());

        // sort list based on comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getKey())
                        .compareTo(((Map.Entry) (o1)).getKey());
            }
        });
        // put sorted list into map again
        // LinkedHashMap make sure order in which keys were inserted
        Map<Long, IndividualDetail> sortedMap = new LinkedHashMap<Long, IndividualDetail>();
        for (Iterator<Entry<Long, IndividualDetail>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Long, IndividualDetail> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /*
     * public void sortAndTotalDetails() {
     * this.uniqueUsers = sortDetailsMap(this.uniqueUsers);
     * this.totalUniqueUsers = this.uniqueUsers.size();
     * this.timeZones = sortDetailsMap(this.timeZones);
     * this.totalTimeZones = this.timeZones.size();
     * }
     * 
     * public void trimUsers(Integer count) {
     * this.uniqueUsers = trimDetailsMap(this.uniqueUsers, count);
     * }
     * 
     * @SuppressWarnings({ "unchecked", "rawtypes" })
     * private static Map<String, Integer> sortDetailsMap(Map<String, Integer>
     * unsortMap) {
     * 
     * List<Entry<String, Integer>> list = new LinkedList<Map.Entry<String,
     * Integer>>(unsortMap.entrySet());
     * 
     * // sort list based on comparator
     * Collections.sort(list, new Comparator() {
     * public int compare(Object o1, Object o2) {
     * return ((Comparable) ((Map.Entry) (o2)).getValue())
     * .compareTo(((Map.Entry) (o1)).getValue());
     * }
     * });
     * 
     * // put sorted list into map again
     * // LinkedHashMap make sure order in which keys were inserted
     * Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
     * for (Iterator<Entry<String, Integer>> it = list.iterator();
     * it.hasNext();) {
     * Map.Entry<String, Integer> entry = it.next();
     * sortedMap.put(entry.getKey(), entry.getValue());
     * }
     * return sortedMap;
     * }
     * 
     * private static Map<String, Integer> trimDetailsMap(Map<String, Integer>
     * untrimmedMap, int count) {
     * Map<String, Integer> trimmedMap = new LinkedHashMap<String, Integer>();
     * 
     * if (count < untrimmedMap.size()) {
     * Integer i = 0;
     * for (String key : untrimmedMap.keySet()) {
     * if (!(i < count)) {
     * break;
     * }
     * trimmedMap.put(key, untrimmedMap.get(key));
     * i++;
     * }
     * }
     * 
     * return trimmedMap;
     * }
     */
}
