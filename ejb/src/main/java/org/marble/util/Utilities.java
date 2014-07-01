package org.marble.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Utilities {
    public static String getDatedMessage(String message) {
        DateFormat dateFormat = new SimpleDateFormat(Constants.LONG_DATE_FORMATTER);
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
