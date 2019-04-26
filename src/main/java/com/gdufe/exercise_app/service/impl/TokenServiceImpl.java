package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.dao.TokenMapper;
import com.gdufe.exercise_app.service.TokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-18
 */
@Service
public class TokenServiceImpl extends ServiceImpl<TokenMapper, Token> implements TokenService {

}
