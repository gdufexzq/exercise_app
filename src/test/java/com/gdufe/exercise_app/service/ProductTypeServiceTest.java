package com.gdufe.exercise_app.service;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.ProductType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class ProductTypeServiceTest extends ExerciseAppApplicationTests {

    @Autowired
    private ProductTypeService productTypeService;

    @Test
    public void testType(){
        ProductType productType = new ProductType();
        productType.setCreateTime(new Date().getTime());
        productType.setModifyTime(new Date().getTime());
        productType.setProductCount(100L);
        productType.setProductType("S");
        productType.setProductId(8L);
        productTypeService.save(productType);
        ProductType productType1 = new ProductType();
        productType1.setCreateTime(new Date().getTime());
        productType1.setModifyTime(new Date().getTime());
        productType1.setProductCount(100L);
        productType1.setProductType("M");
        productType1.setProductId(8L);
        productTypeService.save(productType1);
        ProductType productType2 = new ProductType();
        productType2.setCreateTime(new Date().getTime());
        productType2.setModifyTime(new Date().getTime());
        productType2.setProductCount(100L);
        productType2.setProductType("L");
        productType2.setProductId(8L);
        productTypeService.save(productType2);

    }

}