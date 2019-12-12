package com.threesing.macau.Support;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeZone {

    private String TAG = "TimeZone";

    public TimeZone(){
        super();
    }

    public String getDateTime() {
        Date date = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateTime.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4")); //美東時區
        Log.e(TAG, "dateTime = " + dateTime);
        return dateTime.format(date);
    }
}
