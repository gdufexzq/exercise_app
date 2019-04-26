package com.gdufe.exercise_app.controller;

import com.gdufe.exercise_app.aop.TokenAuth;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAutoProxyController {

    @RequestMapping("/testAuth")
    @TokenAuth
    public String testAuth(@RequestParam String token) {
        System.out.println(token);
        return token;
    }
}
