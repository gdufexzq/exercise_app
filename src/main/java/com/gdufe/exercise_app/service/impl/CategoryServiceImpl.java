package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.Category;
import com.gdufe.exercise_app.dao.CategoryMapper;
import com.gdufe.exercise_app.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-19
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
