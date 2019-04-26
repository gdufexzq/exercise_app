package com.gdufe.exercise_app.aop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class AuthTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private static final String authFieldName = "token";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        //检查是否有TokenAuth注释，有则认证
        if (method.isAnnotationPresent(TokenAuth.class)) {

            String token = request.getParameter("token");// 取出 token
            QueryWrapper tokenWrapper = new QueryWrapper();
            tokenWrapper.eq("token", token);
            List<Token> tokenList = tokenService.list();
            if( null == tokenList){
                return false;
            }else {
                return true;
            }
//            String token = request.getParameter(authFieldName);// 取出 token
//            Code2Session code2Session = TokenContainer.get(token);
//            if (null == code2Session) {
//                logger.info("[token failure, token: {}]", token);
//                throw new TokenErrorException("token无效");
//            }
//            if(code2Session.getGetTime() + code2Session.getExpiresIn() <= System.currentTimeMillis()) {
//                logger.info("[token overdue, token: {}]", token);
//                throw new TokenErrorException("token已过期");
//            }
        }
        return true;
    }

}
