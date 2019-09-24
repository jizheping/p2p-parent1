package com.jizheping.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestDateAdd {
    public static void main(String[] args) throws ParseException {
        Calendar fromCal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("(yyyy-MM-dd HH:mm:ss)");

        Date date = new Date();
        String dateString = dateFormat.format(date);
        System.out.println("date===" + dateString);
        date = dateFormat.parse(dateString);
        fromCal.setTime(date);
        fromCal.add(Calendar.DATE,3);
        dateString = dateFormat.format(fromCal.getTime());
        System.out.println("date.add===" + dateString);
    }
}
