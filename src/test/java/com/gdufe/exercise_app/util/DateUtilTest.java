package com.gdufe.exercise_app.util;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.*;

public class DateUtilTest {

    @Test
    public void dayForWeek() throws ParseException {
        int week = DateUtil.dayForWeek(new Date().getTime());
        System.out.println(week);
    }

    @Test
    public void testLongToString(){
//        1552873587660
        String yearmonthday = DateUtil.longToString(1552873587660L);
        System.out.println(yearmonthday);
        String year = yearmonthday.substring(0,4);
        String month = yearmonthday.substring(5,7);
    }
}