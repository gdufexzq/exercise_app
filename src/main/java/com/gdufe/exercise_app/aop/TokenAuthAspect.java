package com.gdufe.exercise_app.aop;

import com.baomidou.mybatisplus.core.override.PageMapperMethod;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 切面前置增强,暂时没用到
 */
@Aspect
@Component
public class TokenAuthAspect {
    @Pointcut("@annotation(TokenAuth)")
    public void annotationPointCut() {
    }


    @Before("annotationPointCut()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs(); // 参数值String[]
        System.out.println(Arrays.asList(args));
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames(); // 参数名
        System.out.println(Arrays.asList(argNames));
        String name = joinPoint.getSignature().getName();
        System.out.println(name);
        System.out.println(args);System.out.println(argNames);
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        TokenAuth annotation = method.getAnnotation(TokenAuth.class);
        System.out.println("日志");

    }
}
