package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/coinTotal")
public class CoinTotalController {

    @Autowired
    private TokenService tokenService;


    @Autowired
    private CoinTotalService coinTotalService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinDetailService coinDetailService;

    /**
     * 查询用户累计所得到的运动币
     */
    @RequestMapping("/totalCoin")
    public String totalCoin(@RequestParam String token){
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper totalCoinWrapper = new QueryWrapper();
        totalCoinWrapper.ge("open_id", openId);
        List totalList = coinTotalService.list(totalCoinWrapper);
        if(totalList.size() == 0) {
            return "0";
        }else {
            CoinTotal totalCoinInfo = (CoinTotal) totalList.get(0);
            return String.valueOf(totalCoinInfo.getCoinTotal());
        }
    }

    /**
     * 观看健身视频，更新运动币的数量和总训练次数
     */
    @RequestMapping("/updateCoinExercise")
    @TokenAuth
    public String updateCoinExercise(@RequestParam String token, @RequestParam String beizhu) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        /**
         * 更新获取金币的记录
         */
        updateCoinDetail(openId, 200, beizhu);

        updateCoinTotal(openId, 200);

//        updateExerciseTotal(openId);

        return "更新成功";
    }

    //跟新金币详情
    public void updateCoinDetail(String openId, int coin, String beizhu) {
        CoinDetail coinDetail = new CoinDetail();
        String userInfoName = getUserInfoName(openId);
        coinDetail.setUserName(userInfoName)
                .setOpenId(openId)
                .setCreateTime(new Date().getTime())
                .setModifyTime(new Date().getTime())
                .setBeizhu(beizhu)
                .setCoin(200L);
        coinDetailService.save(coinDetail);
    }

    public void updateCoinTotal(String openId, int coin) {
        QueryWrapper coinQueryWrapper = new QueryWrapper();
        coinQueryWrapper.eq("open_id", openId);
        List coinTotallist = coinTotalService.list(coinQueryWrapper);

        if(coinTotallist != null && coinTotallist.size() !=0) {
            CoinTotal coinTotal = (CoinTotal) coinTotallist.get(0);
            coinTotal.setCoinTotal(coinTotal.getCoinTotal() + 200);
            coinTotalService.saveOrUpdate(coinTotal);
        } else {
            String userInfoName = getUserInfoName(openId);
            CoinTotal coinTotal = new CoinTotal();
            coinTotal.setCoinTotal(200L)
                    .setModifyTime(new Date().getTime())
                    .setCreateTime(new Date().getTime())
                    .setUserName(userInfoName)
                    .setOpenId(openId);
            coinTotalService.save(coinTotal);
        }
    }

    public String getUserInfoName(String openId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = userService.list(userQueryWrapper).get(0);
        return user.getUserName();
    }

}

