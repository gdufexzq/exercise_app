package com.gdufe.exercise_app.service;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.Category;
import com.gdufe.exercise_app.entity.Product;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class ServiceTest extends ExerciseAppApplicationTests {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    /**
     * 添加目录
     */
    @Test
    public void testCategory(){
        Category category = new Category();
        category.setCategoryName("女士服饰");
        category.setCreateTime(new Date().getTime());
        category.setModifyTime(new Date().getTime());
//        categoryService.save(category);
    }

    @Test
    public void testProduct(){
        Product product = new Product();
        product.setCategoryId(4L);
        product.setCreateTime(new Date().getTime());
        product.setProductSale(0L);
        product.setModifyTime(new Date().getTime());
        product.setProductCount(500L);
        product.setProductDetail("大码网纱罩衫运动t恤女夏 中长款宽松跑步健身速干上衣胖mm瑜伽服");
        product.setProductName("网纱罩衫");
        product.setProductPrice(69L);
        product.setProductImage("//g-search3.alicdn.com/img/bao/uploaded/i4/i1/2114159417/O1CN01MPVTYp2JR2E7iIYZi_!!0-item_pic.jpg_360x360Q90.jpg_.webp"
        );
        productService.save(product);
    }
}