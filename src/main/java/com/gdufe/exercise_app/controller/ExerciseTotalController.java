package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.ExerciseTotal;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.service.ExerciseTotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-31
 */
@RestController
@RequestMapping("/exerciseTotal")
public class ExerciseTotalController {

    @Autowired
    private ExerciseTotalService exerciseTotalService;

    @RequestMapping("/totalExercise")
    @TokenAuth
    public int totalExercise(@RequestParam String token) {
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("open_id", openId);

        List<ExerciseTotal> list = exerciseTotalService.list(queryWrapper);
        if(list != null && list.size() != 0) {
            ExerciseTotal exerciseTotal = list.get(0);
            Integer exerciseTotalcount = exerciseTotal.getExerciseTotalcount();
            return exerciseTotalcount;
        }
        return 0;
    }
}

