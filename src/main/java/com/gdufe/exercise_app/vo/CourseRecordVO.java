package com.gdufe.exercise_app.vo;

import lombok.Data;

@Data
public class CourseRecordVO {

    //最近一次运动时间
    public String date;
    //健身项目
    public String courseName;
    //总累计次数
    public int count;
}
