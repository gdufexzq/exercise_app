package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.ProductType;
import com.gdufe.exercise_app.dao.ProductTypeMapper;
import com.gdufe.exercise_app.service.ProductTypeService;
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
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements ProductTypeService {

}
