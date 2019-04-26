package com.gdufe.exercise_app.service;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.Orders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class OrderServiceTest extends ExerciseAppApplicationTests {

    @Autowired
    private OrdersService orderService;

    @Test
    public void testSave(){

        Orders order = new Orders();
        order.setAddressId(Long.valueOf(2));
        order.setCreateTime(new Date().getTime());

        order.setOpenId("sdfds");
        order.setOrderPrice(Long.valueOf(2343));
        order.setOrderStatus(0L);
        order.setModifyTime(new Date().getTime());
        orderService.save(order);
    }

}