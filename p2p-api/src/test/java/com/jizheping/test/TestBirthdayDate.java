package com.jizheping.test;

public class TestBirthdayDate {
    public static void main(String[] args) {
        String birth = "19990101";

        String newBirth = birth.substring(0,4) + "-" + birth.substring(4,6) + "-" +birth.substring(6);

        System.out.println(newBirth);
    }
}
