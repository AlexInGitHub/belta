package com.dmall;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sinan.chen on 2/1/2019.
 */
public class HeapTest {

    public static void main(String[] args) {
//        byte[] content = new byte[300 * 1024* 1024];
        List container1 = new ArrayList(10000);
        List container2 = new ArrayList(10000);

        for(int i=0;i<200;i++) {
            Person p = new Person();
            container1.add(p);
            container2.add(p);
        }

        container2.add(new Person("sinan"));

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {

        }
    }

    static class Person {

        Person() {

        }

        Person(String name) {
            this.name = name;
        }
        int age = 30;
        String name = "alex";
        byte[] content = new byte[1*1024* 1024];
    }
}
