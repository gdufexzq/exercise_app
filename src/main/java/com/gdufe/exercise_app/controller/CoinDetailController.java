package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.CoinDetail;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.service.CoinDetailService;
import com.gdufe.exercise_app.service.TokenService;
import com.gdufe.exercise_app.util.DateUtil;
import com.gdufe.exercise_app.vo.CoinRecordVO;
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
 * @since 2019-03-18
 */
@RestController
@RequestMapping("/coinDetail")
public class CoinDetailController {

    @Autowired
    private CoinDetailService coinDetailService;
    @Autowired
    private TokenService tokenService;


    /**
     * 查询获得运动币的详情
     */
    @RequestMapping("coinDetailList")
    @TokenAuth
    public List coinDetail(@RequestParam String token) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        queryWrapper.orderByDesc("create_time");
        List<CoinDetail> list = coinDetailService.list(queryWrapper);
        ArrayList<CoinRecordVO> coinRecordVOS = new ArrayList<>();
        if(list != null && list.size() != 0) {
            for (CoinDetail coinDetail : list) {
                CoinRecordVO coinRecordVO = new CoinRecordVO();
                Long createTime = coinDetail.getCreateTime();
                String date = DateUtil.longToString(createTime);
                String beizhu = coinDetail.getBeizhu();
                Long coin = coinDetail.getCoin();
                coinRecordVO.setDate(date);
                coinRecordVO.setBeizhu(beizhu);
                coinRecordVO.setCoin(coin);

                coinRecordVOS.add(coinRecordVO);
            }
        }

        return coinRecordVOS;
    }


    /**
     * 查询今天用户所得到的运动币总数
     * @return
     */
    @RequestMapping("/todayCoinDetail")
    @TokenAuth
    public String todayCoinDetail(@RequestParam String token){
        long today_coin = 0;
        String today = DateUtil.longToString(new Date().getTime());
        long today_start = DateUtil.getLong(today + " 00:00:00");
        long today_end = DateUtil.getLong(today + " 23:59:59");
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper coinDetailWrapper = new QueryWrapper();
        coinDetailWrapper.eq("open_id", openId);
        coinDetailWrapper.ge("create_time", today_start);
        coinDetailWrapper.le("create_time", today_end);
        List<CoinDetail> coinList = coinDetailService.list(coinDetailWrapper);
        for (CoinDetail coinDetail : coinList) {
            today_coin += coinDetail.getCoin();
        }
        return String.valueOf(today_coin);
    }


}

