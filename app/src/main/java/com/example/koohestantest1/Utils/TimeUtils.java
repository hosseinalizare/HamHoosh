package com.example.koohestantest1.Utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import saman.zamani.persiandate.PersianDate;

public class TimeUtils {

    private static String TAG = TimeUtils.class.getSimpleName();

    private static long getDurationTimeStamp(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    public static int getDifferenceInDay(Date start, Date end) {
        return (int) ((getDurationTimeStamp(start, end) / (1000 * 60 * 60 * 24)));
    }

    public static int getDifferenceInMinutes(Date start, Date end) {
        return (int) (getDurationTimeStamp(start, end) / (60 * 1000));
    }

    public static int getDifferenceInHours(Date start, Date end) {
        return (int) (getDurationTimeStamp(start, end) / (60 * 60 * 1000));
    }

    public static long getDifferenceInMonths(Date start, Date end) {
        return (int) ((getDurationTimeStamp(start, end) / (1000 * 60 * 60 * 24 * 30L)));
    }


    public static String getDurationExpression(Date start, Date end) {
        long min = getDifferenceInMinutes(start, end);
        long hour = getDifferenceInHours(start, end);
        long day = getDifferenceInDay(start, end);
        long month = getDifferenceInMonths(start, end);

        if (min < 1)
            return " لحظاتی پیش ";

        if (min < 60)
            return min + " دقیقه پیش";

        if (hour < 24)
            return hour + " ساعت پیش";

        if (day < 30)
            return day + " روز پیش ";

        if (month < 12)
            return month + " ماه پیش ";

        return month / 12 + " سال پیش ";

    }


    /*
        mode : defines this date is come from where
            0: server
            1: local
     */
    public static Date getDateFromString(String date, int mode) throws ParseException {
        SimpleDateFormat sdf;
        if (mode == 0) {
            sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else {

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        return sdf.parse(date);
    }

    //gets yyyy-MM-dd'T'HH:mm:ss.SSS and converts it to
    public static Date convertStrToDate(String dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            return sdf.parse(dateTime);
        } catch (Exception e) {
            Log.d(TAG, "convertStrToDate: " + e.getMessage());
            return null;
        }
    }

    @Deprecated
    // Only Accepts yyyy-MM-dd'T'HH:mm:ss.SSS format
    // returns 00:00 format
    public static String getCleanHourAndMinByString(String dateTime) {
        Date date = convertStrToDate(dateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String min = String.format("%02d", calendar.get(Calendar.MINUTE));
        return hour + ":" + min;
    }

    public static String getCleanHourAndMinByStringV2(String dateTime) {
        String[] dirtyWholeData = dateTime.split("T");
        String[] time = dirtyWholeData[1].split(":");
        String hour = time[0];
        String min = time[1];
        return hour + ":" + min;
    }

    public static String getPersianCleanDate(String dateTime) {
        Date date = convertStrToDate(dateTime);
        if (date != null) {
            PersianDate persianDate = new PersianDate(date);
            int year = persianDate.getShYear();
            String month = " " + persianDate.monthName() + " ";
            int day = persianDate.getShDay();
            return day + month + year;
        } else return "Error";
    }
}

