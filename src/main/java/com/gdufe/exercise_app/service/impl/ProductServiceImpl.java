package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.Product;
import com.gdufe.exercise_app.dao.ProductMapper;
import com.gdufe.exercise_app.service.ProductService;
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
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
