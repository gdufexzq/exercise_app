package com.gdufe.exercise_app.util;

import javafx.beans.property.SimpleIntegerProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DateUtil {

    /**
     *
     * 得到当天的时间戳
     * 参数:2019-04-03
     */
    public static long[] getCurDay(String date) {
        String start = date + " 00:00:00";
        String end = date + " 23:59:59";
        long startStamp = getLong(start);
        long endStamp = getLong(end);
        long[] stamps = new long[2];
        stamps[0] = startStamp;
        stamps[1] = endStamp;
        return stamps;

    }

    /**
     * 通过星期天
     * 得到星期一的时间戳
     */
    public static long getMonStamp(long stamp) {


        return -1;
    }

    /**
     * 通过时间戳判断今天是星期几
     * 0-星期天
     * ...
     * 6-星期六
     *
     * @param stamp
     * @return
     * @throws ParseException
     */
    public static int dayForWeek(long stamp) throws ParseException {
//        String date = "2019-03-18";
        String date = longToString(stamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(date));
        int dayForWeek = 0;
        if(calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            dayForWeek = 6;
        } else {
            dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 时间戳long型转为"2019-03-16"格式
     */

    public static String longToString(long stamp) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date(stamp);
        String format = simpleDateFormat.format(date);
        return format;
    }

    //每个月的天数
    public static String  months[] = {"0","31","28","31","30","31","30","31","31","30","31","30","31"};

    //判断该年是否是闰年
    public static boolean isLeap(String year){
        if(Long.valueOf(year) % 100 == 0) {
            if(Integer.valueOf(year) % 400 == 0) {
                return true;
            }else {
                return false;
            }
        }else {
            if(Integer.valueOf(year) % 4 == 0) {
                return true;
            }else {
                return false;
            }
        }

//        return false;
    }

    /**
     * 传入年月，
     * 输出这个月的开始时间戳和结束时间戳
     * @param
     * @return
     */
    public static long[] getLongStamp(String year, String month) {
        long longStamp[] = new long[2];
        long start = -1;
        long end = -1;
        if (!month.equals("02")) {
            String day = months[Integer.valueOf(month)];
            if(month.length() < 2){
                month = "0" + month;
            }
            String date1 = year + "-" + month + "-" + "01 00:00:00";
            String date2 = year + "-" + month + "-" + day + " 23:59:59";
            start = getLong(date1);
            end = getLong(date2);
            longStamp[0] = start;
            longStamp[1] = end;
        }else {
            boolean leap = isLeap(year);
            if(leap){
                String date1 = year + "-" + "02" + "-" + "01 00:00:00";
                String date2 = year + "-" + "02" + "-" + 29 + " 23:59:59";
                start = getLong(date1);
                end = getLong(date2);
                longStamp[0] = start;
                longStamp[1] = end;
            }else {
                String date1 = year + "-" + "02" + "-" + "01 00:00:00";
                String date2 = year + "-" + "02" + "-" + 28 + " 23:59:59";
                start = getLong(date1);
                end = getLong(date2);
                longStamp[0] = start;
                longStamp[1] = end;
            }
        }
        return longStamp;
    }

    /**
    string转为long,通过这个方法可以得到一天之内的时间戳
     */
    public static long getLong(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = dateFormat.parse(date);
            long time = parse.getTime();
            return time;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 判断当前时间是否是0点0分0秒
     */
    public static void getZeroStamp() {
        Calendar c = Calendar.getInstance();
        int hh = c.get(Calendar.HOUR_OF_DAY);
        int mm = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);
        System.out.println(hh + ":" + mm + ":" + ss);

        if (hh==0 && mm==0 && ss==0) {
            System.out.println("零点零分");
        }
    }
    //获取当前的开始时间戳，和结束时间戳
//    public void
    public static void main(String[] args) {
        /*
        1552579200000
        1552665599000
        86399000
         */
//        long start = getLong("2019-03-15 00:00:00");
//        long end = getLong("2019-03-15 23:59:59");
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(end - start);


//        System.out.println(1552665600000L - 1552665599000L); //1000 毫秒
        /*
        1552665600000
        1552751999000
        86399000
         */
//        long start = getLong("2019-03-16 00:00:00");
//        long end = getLong("2019-03-16 23:59:59");
//        System.out.println(start);
//        System.out.println(end);
//        System.out.println(end - start);
//        System.out.println(Integer.valueOf("02"));
//        long[] longStamp = getLongStamp("2004", "02");
//        System.out.println(longStamp[0]);
//        System.out.println(longStamp[1]);
//        long da = longStamp[1] - longStamp[0];
//        System.out.println(da);
//        System.out.println((((da/1000.0)/60)/60)/24);

//        getZeroStamp();

        long aLong = getLong("2019-04-02 00:00:00");
        System.out.println(aLong);
        if(aLong == 1554134400000L) {
            System.out.println("afds");
        }
    }

}
