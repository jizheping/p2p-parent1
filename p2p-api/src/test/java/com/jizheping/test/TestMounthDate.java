package com.jizheping.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestMounthDate {
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateString = simpleDateFormat.format(date);
        System.out.println(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);
        date = calendar.getTime();
        dateString = simpleDateFormat.format(date);
        System.out.println(dateString);
    }
}
