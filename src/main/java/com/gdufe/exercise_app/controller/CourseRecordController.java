package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.service.CourseDetailService;
import com.gdufe.exercise_app.service.CourseInfoService;
import com.gdufe.exercise_app.service.CourseRecordService;
import com.gdufe.exercise_app.service.ExerciseTotalService;
import com.gdufe.exercise_app.util.DateUtil;
import com.gdufe.exercise_app.vo.CoinRecordVO;
import com.gdufe.exercise_app.vo.CourseRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-30
 */
@RestController
@RequestMapping("/courseRecord")
public class CourseRecordController {

    @Autowired
    private CourseRecordService courseRecordService;

    @Autowired
    private CourseInfoService courseInfoService;

    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private ExerciseTotalService exerciseTotalService;

    /**
     * 查看健身记录数
     */
    @RequestMapping("/courseRecordCount")
    @TokenAuth
    public int courseRecordCount(@RequestParam String token) {

        int count = 0;
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);

        List<CourseRecord> list = courseRecordService.list(queryWrapper);
        if(list != null && list.size() != 0) {
            for (CourseRecord courseRecord : list) {
                count += courseRecord.getExerciseCount();
            }
        }

        return count;
    }

    /**
     * 查看健身记录
     * @param token
     * @return
     */
    @RequestMapping("/exerciseRecordList")
    @TokenAuth
    public List exerciseRecordList(@RequestParam String token) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        queryWrapper.orderByDesc("modify_time");
        List<CourseRecord> list = courseRecordService.list(queryWrapper);
        ArrayList<CourseRecordVO> courseRecordVOS = new ArrayList<>();
        if(list != null && list.size() != 0) {
            for (CourseRecord courseRecord : list) {
                CourseRecordVO courseRecordVO = new CourseRecordVO();
                Long modifyTime = courseRecord.getModifyTime();
                String date = DateUtil.longToString(modifyTime);

                Long courseinfoId = courseRecord.getCourseinfoId();
                CourseInfo courseInfo = courseInfoService.getById(courseinfoId);
                String courseName = courseInfo.getCourseName();

                Integer exerciseCount = courseRecord.getExerciseCount();

                courseRecordVO.setDate(date);
                courseRecordVO.setCourseName(courseName);
                courseRecordVO.setCount(exerciseCount);

                courseRecordVOS.add(courseRecordVO);
            }
        }

        return courseRecordVOS;
    }

    /**
     * 显示3条我的课程
     * @param token
     * @return
     */
    @RequestMapping("/myThreeCourseInfo")
    @TokenAuth
    public List myThreeCourseInfo(@RequestParam String token) {
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        List courseInfoList = new ArrayList();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        queryWrapper.last(" limit 3");
        List<CourseRecord> recordlist = courseRecordService.list(queryWrapper);
        for (CourseRecord courseRecord : recordlist) {
            Long courseinfoId = courseRecord.getCourseinfoId();
            CourseInfo courseInfo = courseInfoService.getById(courseinfoId);
            courseInfoList.add(courseInfo);
        }
        return courseInfoList;
    }


    /**
     * 我的课程信息
     * @param token
     * @return
     */
    @RequestMapping("/myCourseInfoList")
    @TokenAuth
    public List myCourseInfoList(@RequestParam String token) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        List courseInfoList = new ArrayList();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        List<CourseRecord> recordlist = courseRecordService.list(queryWrapper);
        for (CourseRecord courseRecord : recordlist) {
            Long courseinfoId = courseRecord.getCourseinfoId();
            CourseInfo courseInfo = courseInfoService.getById(courseinfoId);
            courseInfoList.add(courseInfo);
        }

        return courseInfoList;
    }


    /**
     * 更新总训练次数
     * @param openId
     */
    public void updateExerciseTotal(String openId) {
        QueryWrapper exerciseQueryWrapper = new QueryWrapper();
        exerciseQueryWrapper.eq("open_id", openId);
        List exerciseTotallist = exerciseTotalService.list(exerciseQueryWrapper);
        if(exerciseTotallist != null && exerciseTotallist.size() !=0) {
            ExerciseTotal exerciseTotal = (ExerciseTotal) exerciseTotallist.get(0);
            exerciseTotal.setExerciseTotalcount(exerciseTotal.getExerciseTotalcount() + 1);
            //更新修改时间
            exerciseTotal.setModifyTime(new Date().getTime());
            exerciseTotalService.saveOrUpdate(exerciseTotal);
        }else {
            ExerciseTotal exerciseTotal = new ExerciseTotal();
            exerciseTotal.setExerciseTotalcount(1)
                    .setCreateTime(new Date().getTime())
                    .setModifyTime(new Date().getTime())
                    .setOpenId(openId);
            exerciseTotalService.save(exerciseTotal);
        }

    }

    /**
     * 获取用户特定课程的训练次数
     * @param token
     * @param courseId
     * @return
     */
    @RequestMapping("/getRecordCount")
    @TokenAuth
    public int getRecordCount(@RequestParam String token, @RequestParam long courseId) {
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        queryWrapper.eq("courseInfo_id", courseId);
        List recordList = courseRecordService.list(queryWrapper);

        if(recordList != null && recordList.size() > 0) {
            CourseRecord courseRecord = (CourseRecord) recordList.get(0);
            Integer exerciseCount = courseRecord.getExerciseCount();
            return exerciseCount;
        }else {
            return -1;
        }

    }

    /**
     * 添加用户训练记录
     */
    @RequestMapping("/addcourseRecord")
    @TokenAuth
    public String addcourseRecord(@RequestParam String token, @RequestParam long courseId) {
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        CourseRecord courseRecord = new CourseRecord();
        courseRecord.setCourseinfoId(courseId)
                .setCreateTime(new Date().getTime())
                .setExerciseCount(1)
                .setModifyTime(new Date().getTime())
                .setOpenId(openId);
        courseRecordService.save(courseRecord);

        /**
         * 修改课程训练人数
         */
        updatePersonCount(courseId);

        /**
         * 修改用户的总训练次数
         */
        updateExerciseTotal(openId);

        return "添加训练记录成功";
    }

    /**
     * 修改课程训练人数
     */
    public void updatePersonCount(long courseId) {
        //修改课程信息表中的训练人数
        CourseInfo courseInfo = courseInfoService.getById(courseId);
        courseInfo.setPersonCount(courseInfo.getPersonCount() + 1);
        courseInfo.setModifyTime(new Date().getTime());
        courseInfoService.saveOrUpdate(courseInfo);

        //修改课程详细表中的训练人数
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("courseInfo_id", courseId);
        CourseDetail courseDetail = (CourseDetail) courseDetailService.list(queryWrapper).get(0);
        courseDetail.setPersonCount(courseDetail.getPersonCount() + 1);
        courseDetail.setModifyTime(new Date().getTime());
        courseDetailService.saveOrUpdate(courseDetail);

        return ;
    }


    /**
     * 更新用户训练次数
     */
    @RequestMapping("/updatecourseRecord")
    @TokenAuth
    public String updatecourseRecord(@RequestParam String token, @RequestParam long courseId,
                                     @RequestParam int recordCount) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("courseInfo_id", courseId);
        queryWrapper.eq("open_id", openId);
        CourseRecord courseRecord = (CourseRecord) courseRecordService.list(queryWrapper).get(0);
        courseRecord.setExerciseCount(recordCount);
        courseRecord.setModifyTime(new Date().getTime());
        courseRecordService.saveOrUpdate(courseRecord);

        /**
         * 修改用户的总训练次数
         */
        updateExerciseTotal(openId);

        return "更新用户训练次数成功";
    }

}

