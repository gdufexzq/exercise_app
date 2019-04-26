package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.service.CourseInfoService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-28
 */
@RestController
@RequestMapping("/courseInfo")
public class CourseInfoController {

    @Autowired
    private CourseInfoService courseInfoService;

    @RequestMapping("/courseInfoList")
    @TokenAuth
    public List courseInfoList(@RequestParam String token, @RequestParam long categoryId) {
        QueryWrapper courseWrapper = new QueryWrapper();
        courseWrapper.eq("category_id", categoryId);
        List courseList = courseInfoService.list(courseWrapper);
        return courseList;
    }
}

