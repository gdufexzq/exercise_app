package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.Goodcar;
import com.gdufe.exercise_app.entity.Product;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.service.GoodcarService;
import com.gdufe.exercise_app.service.ProductService;
import com.gdufe.exercise_app.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
@RequestMapping("/goodcar")
public class GoodcarController {

    @Autowired
    private GoodcarService goodcarService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProductService productService;

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

    @RequestMapping("/updateCount")
    @TokenAuth
    public void updateCount(@RequestParam String token, String count, String goodcarId){

        Goodcar goodcar = goodcarService.getById(Long.valueOf(goodcarId));
        goodcar.setProductCount(Long.valueOf(count));
        goodcarService.saveOrUpdate(goodcar);

    }
    @RequestMapping("/goodcarInfo")
    @TokenAuth
    public Object[] goodcarInfo(@RequestParam String token) {

        Object[] goodcars = new Object[2];
        Long totalPrice = 0L;
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper goodcarWrapper = new QueryWrapper();
        goodcarWrapper.eq("open_id", openId);
        goodcarWrapper.eq("isOrder", 0);
        List<Goodcar> goodcarList = goodcarService.list(goodcarWrapper);
        for (Goodcar goodcar : goodcarList) {
            totalPrice += (goodcar.getProductPrice() * goodcar.getProductCount());
        }
        goodcars[0] = goodcarList;
        goodcars[1] = totalPrice;
        return goodcars;
    }

    @RequestMapping("/goodcarAdd")
    @TokenAuth //自定义注解
    public void goodcarAdd(@RequestParam String token, @RequestParam String productId,
                           @RequestParam String color, @RequestParam String type,
                           @RequestParam String count) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        /**
         * 商品的颜色和规格相同则只更新他的数量而不用添加一条记录
         */
        QueryWrapper goodcarWrapper = new QueryWrapper();
        goodcarWrapper.eq("product_id", Long.valueOf(productId));
        goodcarWrapper.eq("isOrder", 0);
        List<Goodcar> goodCarList = goodcarService.list(goodcarWrapper);
        for (Goodcar goodcar : goodCarList) {
            String productColor = goodcar.getProductColor();
            String productType = goodcar.getProductType();
            if(color.equals(productColor) && type.equals(productType)) {
                goodcar.setProductCount(Long.valueOf(count) + goodcar.getProductCount());
                goodcarService.saveOrUpdate(goodcar);
                return;
            }
        }
        Goodcar goodcar = new Goodcar();
        goodcar.setCreateTime(new Date().getTime());
        goodcar.setModifyTime(new Date().getTime());
        goodcar.setOpenId(openId);
        goodcar.setProductColor(color);
        goodcar.setProductCount(Long.valueOf(count));
        goodcar.setProductId(Long.valueOf(productId));
        goodcar.setIschoose(1);//选中
        goodcar.setIsOrder(0);
        Product product = productService.getById(Long.valueOf(productId));

        goodcar.setProductImage(product.getProductImage());
        goodcar.setProductName(product.getProductName());
        goodcar.setProductPrice(product.getProductPrice());
        goodcar.setProductType(type);
        goodcarService.save(goodcar);
    }
}

