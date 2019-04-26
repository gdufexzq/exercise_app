package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.entity.Product;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-19
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductColorService productColorService;
    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private TokenService tokenService;




//    public Token getTokenInfo(String token) {
//        QueryWrapper tokenWrapper = new QueryWrapper();
//        tokenWrapper.eq("token", token);
//        List<Token> tokenInfo = tokenService.list(tokenWrapper);
//
//        if(tokenInfo != null){
//            return tokenInfo.get(0);
//        }else {
//            return null;
//        }
//    }
    /**
     * 获取商品信息
     *
     */
    @RequestMapping("/productInfo")
    public Object[] productInfo(@RequestParam String token, @RequestParam String productId){

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
//        String openId = tokenInfo.getOpen_id();
        if(tokenInfo != null) {
            Object[] productArr = new Object[4];
            Product product = productService.getById(Long.valueOf(productId));
            QueryWrapper imageWrapper = new QueryWrapper();
            imageWrapper.eq("product_id", productId);
            List imageList = productImageService.list(imageWrapper);
            QueryWrapper colorWrapper = new QueryWrapper();
            colorWrapper.eq("product_id", productId);
            List colorList = productColorService.list(colorWrapper);
            QueryWrapper typeWrapper = new QueryWrapper();
            typeWrapper.eq("product_id", productId);
            List typeList = productTypeService.list(typeWrapper);
            productArr[0] = product;
            productArr[1] = imageList;
            productArr[2] = colorList;
            productArr[3] = typeList;
            return productArr;
        }

        return null;
    }


}

