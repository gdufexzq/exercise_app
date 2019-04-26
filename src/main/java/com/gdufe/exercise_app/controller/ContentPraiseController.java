package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.service.ContentPraiseService;
import com.gdufe.exercise_app.service.TokenService;
import com.gdufe.exercise_app.service.UserContentService;
import com.gdufe.exercise_app.service.UserService;
import org.checkerframework.checker.units.qual.A;
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
 * @since 2019-03-25
 */
@RestController
@RequestMapping("/contentPraise")
public class ContentPraiseController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ContentPraiseService contentPraiseService;

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private UserService userService;

    private String praiseIconFocus = "../../resources/images/Ic_Praise_Focus.png";
    private String praiseIconNormal = "../../resources/images/Ic_Praise_Normal.png";


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

    @RequestMapping("/isPraise")
    @TokenAuth
    public int[] isPraise(@RequestParam String token, @RequestParam long contentId) {

        /**
         * 步骤：
         *
         * 1.查看点赞表，看是否存在该用户对该说说的点赞记录，
         * 如不存在，说明是第一次点赞，则创建一条该用户对该说说的点赞记录
         * 如存在，说明已经点赞过，然后判断isPraise,
         * 如为0，说明这次是点赞，则把isPraise置为1，并且要把user_content表的点赞数加1
         * 如为1，说明这次是取消点赞，则把isPraise置为0，并且要把user_content表的点赞数减1
         *
         * 返回值:
         * int[0]:返回是否点赞，0-取消点赞，1-点赞
         * int[1]:返回该条说说的点赞数
         */

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        int[] res = new int[2];

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("content_id", contentId);
        queryWrapper.eq("praise_id", openId);
        List praiselist = contentPraiseService.list(queryWrapper);

        if(praiselist.size() == 0) {
            //第一次点赞， 创建一条该记录
            QueryWrapper<User> userWrapper = new QueryWrapper();
            userWrapper.eq("open_id", openId);
            User user = userService.list(userWrapper).get(0);


            ContentPraise contentPraise = new ContentPraise();
            contentPraise.setContentId(contentId);
            contentPraise.setCreateTime(new Date().getTime());
            contentPraise.setIsPraise(1);
            contentPraise.setModifyTime(new Date().getTime());
            contentPraise.setPraiseId(openId);
            contentPraise.setUserImage(user.getUserImage());
            contentPraise.setUserName(user.getUserName());

            contentPraiseService.save(contentPraise);
            //修改点赞数量

            UserContent userContent = userContentService.getById(contentId);
            int praiseData = userContent.getPraiseData() + 1;
            userContent.setPraiseData(praiseData);
            userContent.setPraiseIcon(praiseIconFocus);
            userContentService.saveOrUpdate(userContent);

            res[0] = 1;
            res[1] = praiseData;
            return res;
        } else {
            //多次点赞
            ContentPraise contentPraise = (ContentPraise) praiselist.get(0);
            Integer isPraise = contentPraise.getIsPraise();
            if(isPraise == 0) {
                //该事件是点赞
                contentPraise.setIsPraise(1);
                contentPraiseService.saveOrUpdate(contentPraise);
                UserContent userContent = userContentService.getById(contentId);
                int praiseData = userContent.getPraiseData() + 1;
                userContent.setPraiseData(praiseData);
                userContent.setPraiseIcon(praiseIconFocus);
                userContentService.saveOrUpdate(userContent);

                res[0] = 1;
                res[1] = praiseData;
                return res;
            }else {
                contentPraise.setIsPraise(0);
                contentPraiseService.saveOrUpdate(contentPraise);
                UserContent userContent = userContentService.getById(contentId);
                int praiseData = userContent.getPraiseData() - 1;
                userContent.setPraiseData(praiseData);
                userContent.setPraiseIcon(praiseIconNormal);
                userContentService.saveOrUpdate(userContent);
                res[0] = 0;
                res[1] = praiseData;
                return res;
            }

        }
    }

}

