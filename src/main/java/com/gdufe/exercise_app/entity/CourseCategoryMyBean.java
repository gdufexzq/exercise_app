package com.gdufe.exercise_app.entity;

import lombok.Data;

import java.util.List;

@Data
public class CourseCategoryMyBean {

    private String imageUrl;
    private String categoryName;
    private int categoryCount;
    private List<CourseMyBean> courseMyBeanList;
}
