package com.gdufe.exercise_app.service.impl;

import com.gdufe.exercise_app.entity.Orders;
import com.gdufe.exercise_app.dao.OrdersMapper;
import com.gdufe.exercise_app.service.OrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-24
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
