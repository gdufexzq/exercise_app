package com.gdufe.exercise_app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.entity.Category;
import com.gdufe.exercise_app.entity.Token;
import com.gdufe.exercise_app.entity.TokenInfo;
import com.gdufe.exercise_app.service.CategoryService;
import com.gdufe.exercise_app.service.ProductService;
import com.gdufe.exercise_app.service.TokenService;
import com.gdufe.exercise_app.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.rmi.PortableRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-19
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProductService productService;

    /**
     * 返回目录信息
     */
    @RequestMapping("/categoryInfo")
    public Object[] categoryInfo(@RequestParam String token, @RequestParam String typeId) {
        Object[] category_product = new Object[2];
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
//        String openId = tokenInfo.getOpen_id();
        if(tokenInfo != null) {
            List<Category> categoryList = categoryService.list();
            Map types = new HashMap<>();
            types.put("types", categoryList);
            category_product[0] = types;
            if(categoryList != null) {
                Category category = categoryList.get(Integer.valueOf(typeId) - 1);
                Long categoryId = category.getId();
                QueryWrapper productWrapper = new QueryWrapper();
                productWrapper.eq("category_id", categoryId);
                List productList = productService.list(productWrapper);
                Map typeList = new HashMap();
                typeList.put("typeList", productList);
                category_product[1] = typeList;
            }
            return category_product;
        }
        return null;
    }

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

}

