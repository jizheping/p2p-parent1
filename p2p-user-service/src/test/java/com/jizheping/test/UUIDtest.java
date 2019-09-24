package com.jizheping.test;

import java.util.UUID;

public class UUIDtest {
    public static void main(String[] args) {
        String pwd = UUID.randomUUID().toString();

        System.out.println(pwd);
    }
}
