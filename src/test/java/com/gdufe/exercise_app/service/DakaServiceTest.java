package com.gdufe.exercise_app.service;
import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.Daka;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class DakaServiceTest extends ExerciseAppApplicationTests {

    @Autowired
    private DakaService dakaService;

    @Test
    public void testSave() {
        //获取当前的时间
        Daka daka = new Daka();
        daka.setUserName("sfsd");
        daka.setCurTime(new Date().getTime());
        daka.setDaka(1);
        daka.setCreateTime(new Date().getTime());
        daka.setModifyTime(new Date().getTime());
        daka.setOpenid("fsfsd");
        System.out.println(daka);
        dakaService.save(daka);
    }

}