package com.gdufe.exercise_app.service;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.CoinDetail;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class CoinDetailServiceTest extends ExerciseAppApplicationTests {

    @Autowired
    private CoinDetailService coinDetailService;

    @Test
    public void testSave(){
        String openId = "ooVES0RPqnsd1MVza4rwcWyKUAfs";
        long qiandaoTime = new Date().getTime();
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setCoin(10L);
        coinDetail.setBeizhu("签到");
        coinDetail.setCreateTime(qiandaoTime);
        coinDetail.setModifyTime(qiandaoTime);
        coinDetail.setOpenId(openId);
        coinDetail.setUserName("　　");

        coinDetailService.save(coinDetail);
    }

}