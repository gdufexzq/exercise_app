package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.User;
import com.gdufe.exercise_app.dao.UserMapper;
import com.gdufe.exercise_app.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
