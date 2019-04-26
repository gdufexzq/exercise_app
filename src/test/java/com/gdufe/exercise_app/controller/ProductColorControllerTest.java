package com.gdufe.exercise_app.controller;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.ProductColor;
import com.gdufe.exercise_app.service.ProductColorService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class ProductColorControllerTest extends ExerciseAppApplicationTests {

    @Autowired
    private ProductColorService productColorService;

    @Test
    public void testColor(){
        ProductColor productColor = new ProductColor();
        productColor.setCreateTime(new Date().getTime());
        productColor.setModifyTime(new Date().getTime());
        productColor.setProductColor("灰色");
        productColor.setProductId(8L);
        productColor.setProudctCount(100L);
        productColorService.save(productColor);
    }

}