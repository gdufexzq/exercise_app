package com.gdufe.exercise_app.controller;

import com.gdufe.exercise_app.ExerciseAppApplicationTests;
import com.gdufe.exercise_app.entity.ProductImage;
import com.gdufe.exercise_app.service.ProductImageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.*;

public class ProductImageControllerTest extends ExerciseAppApplicationTests {

    @Autowired
    private ProductImageService productImageService;

    @Test
    public void testSave(){
        ProductImage productImage = new ProductImage();
        productImage.setCreateTime(new Date().getTime());
        productImage.setModifyTime(new Date().getTime());
        productImage.setProductId(8L);
        String[] images = {
                "//gd4.alicdn.com/imgextra/i3/1986078405/TB2.Iyacd3nyKJjSZFEXXXTTFXa_!!1986078405.jpg_50x50.jpg",
        "//gd4.alicdn.com/imgextra/i4/1986078405/TB2jlCdck7myKJjSZFzXXXgDpXa_!!1986078405.jpg_50x50.jpg",
        "//gd2.alicdn.com/imgextra/i2/1986078405/TB2PR06cd3nyKJjSZFjXXcdBXXa_!!1986078405.jpg_50x50.jpg_.webp",
        "//gd1.alicdn.com/imgextra/i1/1986078405/TB2wDRQcjZnyKJjSZPcXXXqHVXa_!!1986078405.jpg_50x50.jpg_.webp"};
        for(int i=0; i<4; i++){
            productImage.setImageUrl(images[0]);
            productImageService.save(productImage);
        }

    }
}