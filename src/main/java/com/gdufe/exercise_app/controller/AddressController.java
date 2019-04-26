package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.Address;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.service.AddressService;
import com.gdufe.exercise_app.service.TokenService;
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
 * @since 2019-03-19
 */
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AddressService addressService;



//    public Token getTokenInfo(String token) {
//        QueryWrapper tokenWrapper = new QueryWrapper();
//        tokenWrapper.eq("token", token);
//        List<Token> tokenInfo = tokenService.list(tokenWrapper);
//
//        if(tokenInfo != null){
//            return tokenInfo.get(0);
//        }else {
//            return null;
//        }
//    }

    /**
     * 添加用户的收货地址
     */
    @RequestMapping("/addAddress")
    @TokenAuth
    public void addAddress(@RequestParam String token, @RequestParam String addressName,
                           @RequestParam String addressPhone, @RequestParam String addressPCC,
                           @RequestParam String addressDetail) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        Address address = new Address();
        address.setCreateTime(new Date().getTime());
        address.setModifyTime(new Date().getTime());
        address.setOpenId(openId);
        address.setUserName(addressName);
        address.setUserPhone(addressPhone);
        address.setUserProvinceCityCounty(addressPCC);
        address.setUserDetailAddress(addressDetail);
        addressService.save(address);
    }

    @RequestMapping("/addressList")
    @TokenAuth
    public List addressList(@RequestParam String token) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper addressWrapper = new QueryWrapper();
        addressWrapper.eq("open_id", openId);
        List addressList = addressService.list(addressWrapper);

        return addressList;
    }
}

