package com.jizheping.test;

import freemarker.template.SimpleDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();
        System.out.println(date);

        String format = simpleDateFormat.format(date);

        System.out.println(format);

        Date parse = simpleDateFormat.parse(format);

        System.out.println(parse);
    }
}
