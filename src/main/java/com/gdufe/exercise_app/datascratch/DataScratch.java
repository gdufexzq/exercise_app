package com.gdufe.exercise_app.datascratch;

import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.util.HttpUtils;
import org.apache.http.HttpException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.attribute.standard.MediaSize;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DataScratch {

    public static List categoryList = new ArrayList<>();

    public static String courseInfoScratch() throws HttpException {
        String param = "5a6ae7afa29e345d66e611c0";
        HttpUtils.get("https://www.gotokeep.com/workouthashtags/" + param, null);
        return null;
    }

    /**
     * 获取课程目录列表
     * @return
     */
    public String getCategoryList() {
        long start = System.currentTimeMillis();
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(8, 10,
//                0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        try {
            String data = HttpUtils.get("https://www.gotokeep.com/training", null);
            Document doc = Jsoup.parse(data);
            Element workoutHashtag = doc.getElementsByClass("workout-hashtag").get(0);
            Elements liList = workoutHashtag.getElementsByTag("li");

            for (Element li : liList) {
//                threadPoolExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
                try {
                    CourseCategoryMyBean courseCategoryMyBean = getCourseCategory(li);
                    //抓取一个课程信息
                    categoryList.add(courseCategoryMyBean);
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }
//                });
//            }
            System.out.println(categoryList);
        } catch (HttpException e) {
            e.printStackTrace();
        }
//        threadPoolExecutor.shutdown();
//        while(!threadPoolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {}
        long end = System.currentTimeMillis();
        System.out.println("----------------------------:"  + (end-start));
        return null;
    }

    /**
     * 抓取每个目录下的课程信息
     *
     * @return
     * @throws InterruptedException
     */
    public CourseCategoryMyBean getCourseCategory(Element li) throws HttpException {

        Element a = li.getElementsByTag("a").get(0);
        String imageUrl = a.attr("data-background");
        String categoryName = li.getElementsByClass("title").get(0).text();
        String categoryCountStr = li.getElementsByClass("count").get(0).text();
        String categoryCount = categoryCountStr.substring(0, 1);

        CourseCategoryMyBean courseCategoryMyBean = new CourseCategoryMyBean();
        courseCategoryMyBean.setImageUrl(imageUrl);
        courseCategoryMyBean.setCategoryName(categoryName);
        courseCategoryMyBean.setCategoryCount(Integer.valueOf(categoryCount));

        String href = a.attr("href");
        /**
         * 解析课程内容
         */
        String courseInfo = HttpUtils.get("https://www.gotokeep.com" + href, null);
        Document courseInfoDoc = Jsoup.parse(courseInfo);
        Element courses = courseInfoDoc.getElementsByClass("courses").get(0);
        Elements courseInfoList = courses.getElementsByTag("li");
        for (Element courseLi : courseInfoList) {
            /**
             *  抓取每个课程详细信息
             */
            CourseMyBean courseMyBean = getcourseInfo(courseLi);

            if (courseCategoryMyBean.getCourseMyBeanList() == null) {
                List courseMyBeanList = new ArrayList();
                courseCategoryMyBean.setCourseMyBeanList(courseMyBeanList);
            }
            courseCategoryMyBean.getCourseMyBeanList().add(courseMyBean);
        }
        return courseCategoryMyBean;
    }

    /**
     * 抓取每个课程信息
     * @return
     * @throws InterruptedException
     */
    public CourseMyBean getcourseInfo(Element courseLi) throws HttpException {
        Element a1 = courseLi.getElementsByTag("a").get(0);
        String courseUrl = a1.getElementsByTag("div").get(0).attr("data-background");
        String courseName = a1.getElementsByClass("name").get(0).text();
        String courseLevel = a1.getElementsByClass("level").get(0).text();
        CourseMyBean courseMyBean = new CourseMyBean();
        courseMyBean.setImageUrl(courseUrl);
        courseMyBean.setCourseName(courseName);
        courseMyBean.setCourseLevel(courseLevel);

        /**
         * 解析课程详细内容
         */
        String planHref = a1.attr("href");
        CourseDetailInfoMyBean courseDetailInfoMyBean = getcourseDetailInfo(planHref);
        /**
         * 添加每个页面的详细信息
         */
        courseMyBean.setCourseDetailInfoMyBean(courseDetailInfoMyBean);

        return courseMyBean;
        }

    /**
     *
     * 抓取每个课程的详细信息
     * @return
     * @throws HttpException
     */
    public CourseDetailInfoMyBean getcourseDetailInfo(String planHref) throws HttpException {
        CourseDetailInfoMyBean courseDetailInfoMyBean = new CourseDetailInfoMyBean();
        String courseDetailInfo = HttpUtils.get("https://www.gotokeep.com" + planHref, null);
        Document courseDetailInfoDoc = Jsoup.parse(courseDetailInfo);
        Elements workoutTitle = courseDetailInfoDoc.getElementsByClass("workout-title");
        if (workoutTitle != null && workoutTitle.size() != 0) {
            Element workout = workoutTitle.get(0);
            String style = workout.attr("style");
            String[] split = style.split(";");
            Map<String, String> styleMap = new HashMap<>();
            System.out.println(Arrays.toString(split));
            for (int i = 0; i < split.length; i++) {
                int temp = split[i].indexOf(":");
                System.out.println(temp);
                String temp1 = split[i].substring(0, temp);
                String temp2 = split[i].substring(temp + 1);
//              System.out.println(temp1 + "^_^" + temp2);
                styleMap.put(temp1, temp2.substring(4, temp2.length() - 1));
            }
            String url = styleMap.get("background-image");
            String title = workout.getElementsByClass("title").get(0).text();
            String context = workout.getElementsByTag("p").get(0).text();
            String level = workout.getElementsByTag("span").get(0).text();
            courseDetailInfoMyBean.setImageUrl(url);
            courseDetailInfoMyBean.setContext(context);
            courseDetailInfoMyBean.setLevel(level);
            courseDetailInfoMyBean.setName(title);
        }
        Elements workouts = courseDetailInfoDoc.getElementsByClass("workout");
        if (workouts != null && workouts.size() != 0) {
            Element workout = workouts.get(0);
            Elements clearfixs = workout.getElementsByClass("clearfix");
            int size = clearfixs.size();
            for (int i = 0; i < size; i++) {
                Element clearfix = clearfixs.get(i);
                /**
                 * 抓取每个动作的信息
                 */
                ActivityInfoMyBean activityInfoMyBean = getActivityInfo(clearfix);
                /**
                 * 添加每个动作的详细信息
                 */
                if (courseDetailInfoMyBean.getActivityInfoMyBeanList() == null) {
                    List courseDetailInfoMyBeanList = new ArrayList();
                    courseDetailInfoMyBean.setActivityInfoMyBeanList(courseDetailInfoMyBeanList);
                }
                courseDetailInfoMyBean.getActivityInfoMyBeanList().add(activityInfoMyBean);
            }
        }
        return courseDetailInfoMyBean;
    }

    public ActivityInfoMyBean getActivityInfo(Element clearfix) throws HttpException {
        //包括父div，所以下标是1，而不是0
        Element div = clearfix.getElementsByTag("div").get(1);
        String attr = div.attr("data-background");
        String name = clearfix.getElementsByClass("name").get(0).text();
        String payload = clearfix.getElementsByClass("payload").get(0).text();
        ActivityInfoMyBean activityInfoMyBean = new ActivityInfoMyBean();
        activityInfoMyBean.setImageUrl(attr);
        activityInfoMyBean.setName(name);
        activityInfoMyBean.setPayload(payload);
        /**
         * 获取动作视频的url
         */
        Element a = clearfix.getElementsByTag("a").get(0);
        String href = a.attr("href");
        String videoUrl = getVideoUrl(href);
        activityInfoMyBean.setVideoUrl(videoUrl);

        return activityInfoMyBean;
    }
    public String getVideoUrl(String href) throws HttpException {

        String str = HttpUtils.get("https://www.gotokeep.com" + href, null);
        Document doc = Jsoup.parse(str);
        Elements exercises = doc.getElementsByClass("exercise");
        if (exercises != null && exercises.size() != 0) {
            Element exercise = exercises.get(0);
            Element div = exercise.getElementsByTag("div").get(2);

            String attr = div.attr("data-src");
            System.out.println("url: " + attr);
            return attr;
        }
        return null;
    }
    public String courseScratch() throws InterruptedException {
        return getCategoryList();
    }

    public static void main(String[] args) throws InterruptedException {
        new DataScratch().courseScratch();
    }
}
