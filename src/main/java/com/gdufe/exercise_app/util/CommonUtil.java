package com.gdufe.exercise_app.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class CommonUtil {


    @Autowired
    private static TokenService tokenService;

    /**
     * 获取token信息,可通过这个方法，顺便判断token是否存在
     * @param token
     * @return
     */
    public static Token getTokenInfo(String token) {
        QueryWrapper tokenWrapper = new QueryWrapper();
        tokenWrapper.eq("token", token);
        List<Token> tokenInfo = tokenService.list(tokenWrapper);

        if(tokenInfo != null){
            return tokenInfo.get(0);
        }else {
            return null;
        }
    }
}
