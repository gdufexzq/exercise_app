package com.gdufe.exercise_app.entity;

import lombok.Data;

import java.util.List;
@Data
public class CourseDetailInfoMyBean {

    private String imageUrl;
    private String context;
    private String level;
    private String name;
    private List<ActivityInfoMyBean> activityInfoMyBeanList;

}
