package com.gdufe.exercise_app.datascratch;

import com.gdufe.exercise_app.util.HttpUtils;
import org.apache.http.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DataScratchTest {

    public void getData() throws HttpException {

        String courseDetailInfo = HttpUtils.get("https://www.gotokeep.com/plans/5a5ec66da29e343cc86d2a4e", null);
        Document courseDetailInfoDoc = Jsoup.parse(courseDetailInfo);
        Elements workouts = courseDetailInfoDoc.getElementsByClass("workout-title");
//        System.out.println("workouts:" + workouts.html());
        if(workouts != null && workouts.size() != 0) {
            Element workout = workouts.get(0);
//          System.out.println("workout:" + workout.html());
            String style = workout.attr("style");
            String[] split = style.split(";");
            Map<String, String> styleMap = new HashMap<>();
            System.out.println(Arrays.toString(split));
            for(int i=0; i<split.length; i++) {
                int temp = split[i].indexOf(":");
                System.out.println(temp);
                String temp1 = split[i].substring(0, temp);
                String temp2 = split[i].substring(temp+1);
                System.out.println(temp1 + "^_^" + temp2);
//                String[] split1 = split[i].split(":");
                styleMap.put(temp1, temp2);
            }

            String url = styleMap.get("background-image");
            System.out.println(url.substring(4,url.length()-1));
            System.out.println("test:" + url);

        }
    }

    public void getData2() throws HttpException {
        String courseDetailInfo = HttpUtils.get("https://www.gotokeep.com/exercises/595f4e62ff247f33e297eaf9?gender=f", null);
        Document courseDetailInfoDoc = Jsoup.parse(courseDetailInfo);
        Elements exercises = courseDetailInfoDoc.getElementsByClass("exercise");
//        System.out.println("workouts:" + workouts.html());
        if (exercises != null && exercises.size() != 0) {
            Element exercise = exercises.get(0);
            Element div = exercise.getElementsByTag("div").get(2);

            String attr = div.attr("data-src");
            System.out.println("url: " + attr);
        }
    }
    public static void main(String[] args) throws HttpException {
        new DataScratchTest().getData2();
    }
}
