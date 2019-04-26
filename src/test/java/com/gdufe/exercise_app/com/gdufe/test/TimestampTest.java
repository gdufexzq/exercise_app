package com.gdufe.exercise_app.com.gdufe.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimestampTest {

    public static void main(String[] args) {
//        System.out.println(new Date(1552623025705l));
//        System.out.println(new Date()); // 1552623025705
//        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
//        System.out.println(new Date().getDay());


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String format = simpleDateFormat.format("2019-03-15");
        System.out.println(format);


    }
}
