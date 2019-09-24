package com.jizheping.test;

import java.util.Collections;
import java.util.LinkedList;

public class Test {
    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<String>();

        list.add("appple");
        list.add("grape");
        list.add("banana");
        list.add("pear");

        String max = Collections.max(list);

        System.out.println("max==="+max);

        String min = Collections.min(list);

        System.out.println("min==="+min);

        Collections.sort(list);

        for(String s : list){
            System.out.println(s+" ");
        }
    }
}
