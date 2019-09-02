package com.alisha.roomfinderapp.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TextDataUtils {
    private static final String TAG = "TextDataUtils";

    public static boolean isValidPassword(String password) {
        Log.d(TAG, "isValidPassword: length: " + password.length());
        return password.length() > 5;
    }

    public static boolean isEmpty(String string) {
        return string.length() == 0;
    }

    /**
     * from the input string, loop through all the characters and if # tag is found return a string
     * containing all the tags else return same string
     *
     * @param string
     * @return (eg ; # tag1, # tag2 from string - > this is desc # tag1 # tag2
     */
    public static String getTags(String string) {
        if (string.indexOf("#") > 0) {
            StringBuilder sb = new StringBuilder();
            char[] charArray = string.toCharArray();
            boolean foundWord = false;
            for (char c : charArray) {
                if (c == '#') {
                    foundWord = true;
                    sb.append(c);
                } else {
                    if (foundWord) {
                        sb.append(c);
                    }
                }
                if (c == ' ') {
                    foundWord = false;
                }
            }
            String s = sb.toString().replace(" ", "").replace("#", ",#");
            return s.substring(0, s.length());
        }
        return string;
    }



    /**
     * Returns a string representing the number of days ago the post was made
     *
     * @return
     */
    public static String getTimestampDifference(String date) {
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kathmandu"));
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp;

        try {
            timestamp = sdf.parse(date);
            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24)));
        } catch (ParseException e) {
            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage());
            difference = "0";
        }

        String days = "";
        if (difference.equals("0")) {
            days = "TODAY";
        } else {
            days = difference + " DAYS AGO";
        }
        return days;
    }

    public static String getTimestampDifference2(String date) {


        int difference;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kathmandu"));
        Date today = c.getTime();
        sdf.format(today);
        Date timestamp = null;
        try {
            timestamp = sdf.parse(date);
        } catch (ParseException e) {
            return "0";
        }
        difference = (int) (((today.getTime() - timestamp.getTime())) / 1000 / 60 / 60 / 24);
        String days = "";
        if (difference == 0) {
            days = "TODAY";
        } else if (difference > 0) {
            days = difference + " DAYS AGO";
        } else {
            days = difference + " DAYS LEFT";
        }
        return days;
    }


    public static String formatDate(int y, int m, int d) {

        try {
            String date = d + "/" + (m + 1) + "/" + y;
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
            DateFormat fullDf = DateFormat.getDateInstance(DateFormat.FULL);

            return fullDf.format(date1);

        } catch (Exception e) {

        }

        return null;
    }


    public static int currentYear() {

        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public static int currentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH);
    }

    public static int currentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static int currentHour() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int currentMin() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MINUTE);
    }


}
