package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.ContentComment;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.entity.User;
import com.gdufe.exercise_app.service.ContentCommentService;
import com.gdufe.exercise_app.service.TokenService;
import com.gdufe.exercise_app.service.UserService;
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
@RequestMapping("/contentComment")
public class ContentCommentController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ContentCommentService contentCommentService;

    @Autowired
    private UserService userService;



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

    @RequestMapping("/addComment")
    @TokenAuth
    public String addComment(@RequestParam String token, @RequestParam long contentId,
                             @RequestParam String context) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);
        User user = userService.list(queryWrapper).get(0);

        ContentComment contentComment = new ContentComment();
        contentComment.setCommentContent(context);
        contentComment.setContentId(contentId);
        contentComment.setCreateTime(new Date().getTime());
        contentComment.setModifyTime(new Date().getTime());
        contentComment.setUserId(openId);
        contentComment.setUserName(user.getUserName());
        contentComment.setUserImage(user.getUserImage());

        contentCommentService.save(contentComment);


        return "发送评论成功";
    }

}

