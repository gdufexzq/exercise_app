package com.gdufe.exercise_app.service;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.datascratch.DataScratch;
import com.gdufe.exercise_app.entity.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CourseCategoryServiceTest extends ExerciseAppApplicationTests {

    @Autowired
    private CourseCategoryService courseCategoryService;

    @Autowired
    private CourseInfoService courseInfoService;

    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private CourseActivityService courseActivityService;

    @Before
    public void beforeSave() throws InterruptedException {
        new DataScratch().courseScratch();
        System.out.println(DataScratch.categoryList);
    }
    @Test
    public void save(){
        List<CourseCategoryMyBean> categoryList = DataScratch.categoryList;
        for (CourseCategoryMyBean courseCategoryMyBean : categoryList) {
            /**
             * 存储目录信息
             */
            String imageUrl = courseCategoryMyBean.getImageUrl();
            String categoryName = courseCategoryMyBean.getCategoryName();
            int categoryCount = courseCategoryMyBean.getCategoryCount();
            CourseCategory courseCategory = new CourseCategory();
            courseCategory.setImageUrl(imageUrl);
            courseCategory.setCategoryName(categoryName);
            courseCategory.setCategoryCount(categoryCount);
            courseCategory.setCreateTime(new Date().getTime());
            courseCategory.setModifyTime(new Date().getTime());
            courseCategoryService.save(courseCategory);

            /**
             * 存储课程信息
             */
            Long id = courseCategory.getId();
            List<CourseMyBean> courseMyBeanList = courseCategoryMyBean.getCourseMyBeanList();
            for (CourseMyBean courseMyBean : courseMyBeanList) {
                CourseInfo courseInfo = new CourseInfo();
                courseInfo.setCategoryId(id);
                courseInfo.setCourseLevel(courseMyBean.getCourseLevel());
                courseInfo.setCourseName(courseMyBean.getCourseName());
                courseInfo.setCreateTime(new Date().getTime());
                courseInfo.setImageUrl(courseMyBean.getImageUrl());
                courseInfo.setModifyTime(new Date().getTime());
                courseInfo.setPersonCount(0);
                courseInfoService.save(courseInfo);

                /**
                 * 存储课程详细信息
                 */
                Long courseInfoId = courseInfo.getId();
                CourseDetailInfoMyBean courseDetailInfoMyBean = courseMyBean.getCourseDetailInfoMyBean();
                if(courseDetailInfoMyBean != null) {

                    CourseDetail courseDetail = new CourseDetail();
                    courseDetail.setCourseContext(courseDetailInfoMyBean.getContext());
                    courseDetail.setCourseinfoId(courseInfoId);
                    courseDetail.setCourseLevel(courseDetailInfoMyBean.getLevel());
                    courseDetail.setCourseName(courseDetailInfoMyBean.getName());
                    courseDetail.setCreateTime(new Date().getTime());
                    courseDetail.setImageUrl(courseDetailInfoMyBean.getImageUrl());
                    courseDetail.setModifyTime(new Date().getTime());
                    courseDetail.setPersonCount(0);
                    courseDetailService.save(courseDetail);


                    /**
                     * 存储动作信息
                     */
                    Long courseDetailId = courseDetail.getId();
                    List<ActivityInfoMyBean> activityInfoMyBeanList = courseDetailInfoMyBean.getActivityInfoMyBeanList();
                    if(activityInfoMyBeanList != null && activityInfoMyBeanList.size() != 0) {
                        for (ActivityInfoMyBean activityInfoMyBean : activityInfoMyBeanList) {
                            CourseActivity courseActivity = new CourseActivity();
                            courseActivity.setActivityName(activityInfoMyBean.getName());
                            courseActivity.setActivityPayload(activityInfoMyBean.getPayload());
                            courseActivity.setCoursedetailId(courseDetailId);
                            courseActivity.setCreateTime(new Date().getTime());
                            courseActivity.setImageUrl(activityInfoMyBean.getImageUrl());
                            courseActivity.setModifyTime(new Date().getTime());
                            courseActivity.setVideoUrl(activityInfoMyBean.getVideoUrl());
                            courseActivityService.save(courseActivity);
                        }
                    }
                }


            }
        }
    }

}