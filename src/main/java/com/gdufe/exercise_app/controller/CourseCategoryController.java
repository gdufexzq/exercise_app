package com.gdufe.exercise_app.controller;


import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.CourseCategory;
import com.gdufe.exercise_app.service.CourseCategoryService;
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
 * @since 2019-03-28
 */
@RestController
@RequestMapping("/courseCategory")
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService courseCategoryService;

    @RequestMapping("/categoryList")
    @TokenAuth
    public List categoryList(@RequestParam String token) {
        List<CourseCategory> courseCategoryList = courseCategoryService.list();
        return courseCategoryList;
    }

    @RequestMapping("/courseFourList")
    @TokenAuth
    public List courseFourList(@RequestParam String token) {
        CourseCategory category1 = courseCategoryService.getById(41);
        CourseCategory category2 = courseCategoryService.getById(69);
        CourseCategory category3 = courseCategoryService.getById(82);
        CourseCategory category4 = courseCategoryService.getById(99);
        ArrayList<CourseCategory> categoryArrayList = new ArrayList<CourseCategory>();
        categoryArrayList.add(category1);
        categoryArrayList.add(category2);
        categoryArrayList.add(category3);
        categoryArrayList.add(category4);
        return categoryArrayList;
    }
}

