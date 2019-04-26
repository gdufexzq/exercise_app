package com.gdufe.exercise_app.entity;

import lombok.Data;

@Data
public class CourseMyBean {

    private String imageUrl;
    private String courseName;
    private String courseLevel;
    private CourseDetailInfoMyBean courseDetailInfoMyBean;
}
