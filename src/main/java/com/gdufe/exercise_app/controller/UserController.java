package com.gdufe.exercise_app.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.entity.User;
import com.gdufe.exercise_app.service.TokenService;
import com.gdufe.exercise_app.service.UserService;
import com.gdufe.exercise_app.util.AESUtil;
import com.gdufe.exercise_app.util.MD5Utils;
import com.gdufe.exercise_app.util.OpenIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-13
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/userInfo")
    public User getUserInfo(@RequestParam String token) {

        TokenInfo tokenInfoMap = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String open_id = tokenInfoMap.getOpen_id();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", open_id);
        List list = userService.list(queryWrapper);
        if(list != null && list.size() != 0) {
            User user = (User) list.get(0);
            return user;
        }
        return null;
    }

    /**
     * 保存用户的token
     * @param code
     * @return
     */
    @RequestMapping("/getToken")
    public String getToken(@RequestParam String code) {
        String jsonObject = OpenIdUtil.getJsonObject(code);
        Map<String, Object> res = (Map<String, Object>) JSON.parse(jsonObject);
        String openId = (String) res.get("openid");
        if(TokenInfoMap.openIdMap.get("openId") != null) {
            //删除用一用户原来的token
            String temp_token = (String) TokenInfoMap.openIdMap.get("openId");
            TokenInfoMap.tokenMap.remove(temp_token);
        }
        String session_key = (String) res.get("session_key");
        String token = MD5Utils.MD5Encode(openId + session_key, null);
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setOpen_id(openId);
        tokenInfo.setSession_key(session_key);
        tokenInfo.setToken(token);
        TokenInfoMap.tokenMap.put(token, tokenInfo);

        TokenInfoMap.openIdMap.put(openId, token);

        return token;
    }

    public int getUser(String openId) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        List list = userService.list(queryWrapper);
        if(list != null && list.size() != 0) {
            return 1;
        }
        return 0;
    }
    @RequestMapping("/addUserInfo")
    public String addUserInfo(@RequestParam String token, @RequestParam String encryptedData, @RequestParam String iv) {
        TokenInfo tokenInfoMap = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String open_id = tokenInfoMap.getOpen_id();
        if(getUser(open_id) == 1) {
            return (String) TokenInfoMap.openIdMap.get(open_id);
        }
        try {

            String session_key = tokenInfoMap.getSession_key();
            String userInfo = AESUtil.getUserInfo(encryptedData, session_key, iv);
            System.out.println(userInfo);
            Map<String, Object> res2 = (Map<String, Object>) JSON.parse(userInfo);
            String openId = (String) res2.get("openId");
            String nickName = (String) res2.get("nickName");
            Integer gender = (Integer) res2.get("gender");
            String province = (String) res2.get("province");
            String country = (String) res2.get("country");
            String avatarUrl = (String) res2.get("avatarUrl");

            token = tokenInfoMap.getToken();

            User user = new User();
            user.setUserName(nickName);
            user.setSex(gender.toString());
            user.setUserImage(avatarUrl);
            user.setUserProvince(province);
            user.setUserCity(country);
            user.setCreateTime(System.currentTimeMillis());
            user.setModifyTime(System.currentTimeMillis());
            user.setOpenId(openId);
            userService.save(user);

            /**
             * 保存token,openId,session_key
             */
//            Token tokenInfo = new Token();
////            tokenInfo.setToken(token);
////            tokenInfo.setOpenId(openId);
////            tokenInfo.setSessionKey(session_key);
////            tokenInfo.setCreateTime(new Date().getTime());
////            tokenInfo.setModifyTime(new Date().getTime());
////
////            tokenService.save(tokenInfo);

            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}

