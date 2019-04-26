package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.exception.ParamErrorException;
import com.gdufe.exercise_app.exception.SystemErrorException;
import com.gdufe.exercise_app.service.*;
import com.gdufe.exercise_app.util.MinioUtil;
import io.minio.MinioClient;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
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
@RequestMapping("/userContent")
public class UserContentController {


    private static Logger logger = LoggerFactory.getLogger(UserContentController.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContentCommentService contentCommentService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private CoinDetailService coinDetailService;

    @Autowired
    private CoinTotalService coinTotalService;

    private static String bucket = "exercise-app";



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

    @RequestMapping("/contentList")
    @TokenAuth
    public List contentList(@RequestParam String token, @RequestParam int isMe) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        List contentTotalList = new ArrayList();


        if(isMe == 0) {
            QueryWrapper wrapper = new QueryWrapper();

            wrapper.ne("open_id", openId);
            wrapper.ne("only_me", isMe);
            wrapper.or();
            wrapper.eq("open_id", openId);
            wrapper.orderByDesc("create_time");

            String sqlSegment = wrapper.getSqlSegment();
            System.out.println(sqlSegment);

            int  count = userContentService.count();
            contentTotalList.add(count);

            List<UserContent> contentList = userContentService.list(wrapper);

            List cList = new ArrayList();

            for (UserContent userContent : contentList) {
                List contentCommentList = new ArrayList();
                Long contentId = userContent.getId();
                QueryWrapper commentWrapper = new QueryWrapper();
                commentWrapper.eq("content_id", contentId);

                List list = contentCommentService.list(commentWrapper);
                contentCommentList.add(userContent);
                contentCommentList.add(list);
                cList.add(contentCommentList);
            }
            contentTotalList.add(cList);
        } else {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("open_id", openId);
            wrapper.orderByDesc("create_time");

            int count = userContentService.count(wrapper);
            contentTotalList.add(count);

            List<UserContent> contentList = userContentService.list(wrapper);
            List cList = new ArrayList();
            for (UserContent userContent : contentList) {
                Long contentId = userContent.getId();
                QueryWrapper commentWrapper = new QueryWrapper();
                commentWrapper.eq("content_id", contentId);

                List list = contentCommentService.list(commentWrapper);
                List contentCommentList = new ArrayList();
                contentCommentList.add(userContent);
                contentCommentList.add(list);
                cList.add(contentCommentList);

            }

            contentTotalList.add(cList);
        }
        return contentTotalList;
    }

    @RequestMapping("/publish")
    @TokenAuth
    public Long publish(@RequestParam String token, @RequestParam String content,
                        @RequestParam String onlyMe, @RequestParam String praiseIcon) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper userWrapper = new QueryWrapper();
        userWrapper.eq("open_id", openId);
        User userInfo = (User) userService.list(userWrapper).get(0);
        String userName = userInfo.getUserName();
        String userImage = userInfo.getUserImage();


        UserContent userContent = new UserContent();
        userContent.setContentContext(content);
        userContent.setCreateTime(new Date().getTime());
        userContent.setModifyTime(new Date().getTime());
        userContent.setOpenId(openId);
        userContent.setPraiseData(0);
        if(onlyMe.equals("true")){
            userContent.setOnlyMe(1);
        }else {
            userContent.setOnlyMe(0);
        }
        userContent.setUserName(userName);
        userContent.setUserImage(userImage);
        userContent.setPraiseIcon(praiseIcon);

        userContentService.save(userContent);

        /**
         * 发表说说，获得100运动币
         */
        getCoin(openId);
        /**
         * 修改运动币总量
         */
        updateCoinTotal(openId);


        Long contentId = userContent.getId();


        return contentId;
    }

    public void updateCoinTotal(String openId) {
        QueryWrapper coinQueryWrapper = new QueryWrapper();
        coinQueryWrapper.eq("open_id", openId);
        List coinTotallist = coinTotalService.list(coinQueryWrapper);

        if(coinTotallist != null && coinTotallist.size() !=0) {
            CoinTotal coinTotal = (CoinTotal) coinTotallist.get(0);
            coinTotal.setCoinTotal(coinTotal.getCoinTotal() + 100);
            coinTotalService.saveOrUpdate(coinTotal);
        } else {
            String userInfoName = getUserInfoName(openId);
            CoinTotal coinTotal = new CoinTotal();
            coinTotal.setCoinTotal(100L)
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

    //发表评论提供运动币100
    public void getCoin(String openId) {
        String userInfoName = getUserInfoName(openId);
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setCoin(100L)
                .setBeizhu("发表评论")
                .setModifyTime(new Date().getTime())
                .setCreateTime(new Date().getTime())
                .setOpenId(openId)
                .setUserName(userInfoName);
        
        coinDetailService.save(coinDetail);
    }

    @RequestMapping("/upload")
    @TokenAuth
    public String upload(@RequestParam String token,
                         @RequestParam(value = "file", required = false) MultipartFile file,
                         @RequestParam long contentId, @RequestParam int imgNo) {

        String originalFileName = file.getOriginalFilename();
        if(file.isEmpty()) {
            throw new ParamErrorException("文件有误");
        }
        try {
            String newImgUrl = MinioUtil.FileUploadByStream(minioClient, bucket,
                    System.currentTimeMillis() + originalFileName, file.getInputStream());

            String updateSql = "content_image=(case when content_image is null then '"
                    + newImgUrl +"' else concat(content_image, '"+( "," +newImgUrl)+"') end) where id="+contentId;
            logger.info("[updateSql: {}]", updateSql);
            UpdateWrapper<UserContent> userContentUpdateWrapper = new UpdateWrapper<>();
            userContentUpdateWrapper.lambda().setSql(updateSql);
            userContentService.update(userContentUpdateWrapper);
//            UserContent userContent = userContentService.getById(contentId);
//            String contentImage = userContent.getContentImage();
//            if(contentImage == null) {
//                userContent.setContentImage(newImgUrl);
//                userContentService.saveOrUpdate(userContent);
//            } else {
//                userContent.setContentImage(contentImage + "," + newImgUrl);
//                userContentService.saveOrUpdate(userContent);
//            }
        } catch (Exception e) {
            throw new SystemErrorException("内部系统出错", e);
        }
        return "上传成功";
    }

}

