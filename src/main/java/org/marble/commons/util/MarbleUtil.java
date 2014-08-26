package org.marble.commons.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MarbleUtil {
    public static String getDatedMessage(String message) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        return dateFormat.format(date) + ": " + message;
    }

    public static Comparator<List<Double>> sortComparator = new Comparator<List<Double>>() {
        @Override
        public int compare(List<Double> arg0, List<Double> arg1) {
            return arg0.get(0).compareTo(arg1.get(0));
        }
    };
}
