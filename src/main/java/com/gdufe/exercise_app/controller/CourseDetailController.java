package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.CourseDetail;
import com.gdufe.exercise_app.service.CourseActivityService;
import com.gdufe.exercise_app.service.CourseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-29
 */
@RestController
@RequestMapping("/courseDetail")
public class CourseDetailController {

    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private CourseActivityService courseActivityService;



    @RequestMapping("/courseDetailInfo")
    @TokenAuth
    public List courseDetailInfo(@RequestParam String token, @RequestParam long courseId) {
        List courseList = new ArrayList();
        QueryWrapper courseWrapper = new QueryWrapper();
        courseWrapper.eq("courseInfo_id", courseId);
        CourseDetail courseDetail = (CourseDetail) courseDetailService.list(courseWrapper).get(0);

        QueryWrapper activityWrapper = new QueryWrapper();
        activityWrapper.eq("courseDetail_id", courseDetail.getId());

        List activityList = courseActivityService.list(activityWrapper);
        courseList.add(courseDetail);
        courseList.add(activityList);
        return courseList;

    }
}

