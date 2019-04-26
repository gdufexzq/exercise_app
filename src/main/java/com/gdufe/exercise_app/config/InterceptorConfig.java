package com.gdufe.exercise_app.config;

import com.gdufe.exercise_app.aop.AuthTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authTokenAOPInterceptor())
                .addPathPatterns("/**"); // 拦截所有请求，通过判断是否有 @AuthToken 注解 决定是否需要验证token
//        registry.addInterceptor(ipInterceptor()) //后台管理接口IP拦截
//                .addPathPatterns("/manager/*");
    }

    @Bean
    public AuthTokenInterceptor authTokenAOPInterceptor() {
        return new AuthTokenInterceptor();
    }
//    @Bean
//    public IpInterceptor ipInterceptor() {
//        return new IpInterceptor();
//    }
}