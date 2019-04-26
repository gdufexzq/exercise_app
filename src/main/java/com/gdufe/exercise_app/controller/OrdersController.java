package com.gdufe.exercise_app.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gdufe.exercise_app.Token.TokenInfoMap;
import com.gdufe.exercise_app.aop.TokenAuth;
import com.gdufe.exercise_app.entity.*;
import com.gdufe.exercise_app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService orderService;


    @Autowired
    private TokenService tokenService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private GoodcarService goodcarService;

    @Autowired
    private CoinTotalService coinTotalService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductColorService productColorService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinDetailService coinDetailService;


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

    @RequestMapping("/productAddOrder")
    @TokenAuth
    public Long productAddOrder(@RequestParam String token, @RequestParam String addressId,
                                @RequestParam String productId, @RequestParam String color,
                                @RequestParam String type, @RequestParam String count,
                                @RequestParam String totalPrice, @RequestParam String productName,
                                @RequestParam String productImage, @RequestParam String productPrice) {
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        Orders order = new Orders();
        order.setAddressId(Long.valueOf(addressId));
        order.setCreateTime(new Date().getTime());
        order.setModifyTime(new Date().getTime());
        order.setOpenId(openId);
        order.setOrderPrice(Long.valueOf(totalPrice));
        order.setOrderStatus(1L);
        orderService.save(order);


        Long id = order.getId();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setCreateTime(new Date().getTime());
        orderDetail.setModifyTime(new Date().getTime());
        orderDetail.setOrderId(id);
        orderDetail.setProductColor(color);
        orderDetail.setProductCount(Long.valueOf(count));
        orderDetail.setProductType(type);
        orderDetail.setProductPrice(Long.valueOf(productPrice));
        orderDetail.setProductImage(productImage);
        orderDetail.setProductName(productName);


        orderDetailService.save(orderDetail);

        return id;
    }


    @RequestMapping("/goodcarAddOrder")
    @TokenAuth
    public Long goodcarAddOrder(@RequestParam String token, @RequestParam String addressId,
                                @RequestParam String totalPrice,
                                @RequestParam String productOrderDetail) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();

        List<Goodcar> goodcars = JSON.parseArray(productOrderDetail, Goodcar.class);
        System.out.println(goodcars);

        Orders order = new Orders();
        order.setAddressId(Long.valueOf(addressId));
        order.setCreateTime(new Date().getTime());
        order.setModifyTime(new Date().getTime());
        order.setOpenId(openId);
        order.setOrderPrice(Long.valueOf(totalPrice));
        order.setOrderStatus(1L);

        orderService.save(order);

        QueryWrapper orderWrapper = new QueryWrapper();
        orderWrapper.orderByDesc("id");
        orderWrapper.last(" limit 1");
//        List<Orders> list = orderService.list(orderWrapper);
        Orders orderInfo = (Orders) orderService.list(orderWrapper).get(0);
        Long id = orderInfo.getId();

        int len = goodcars.size();
        for (int i=0; i<len; i++) {
            //修改购物车产品的状态
            Goodcar goodcar = goodcars.get(i);
            Goodcar goodcarInfo = goodcarService.getById(goodcar.getId());
            goodcarInfo.setIsOrder(1);
            goodcarService.saveOrUpdate(goodcarInfo);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCreateTime(new Date().getTime());
            orderDetail.setModifyTime(new Date().getTime());
            orderDetail.setOrderId(id);
            orderDetail.setProductColor(goodcar.getProductColor());
            orderDetail.setProductCount(Long.valueOf(goodcar.getProductCount()));
            orderDetail.setProductType(goodcar.getProductType());
            orderDetail.setProductPrice(Long.valueOf(goodcar.getProductPrice()));
            orderDetail.setProductImage(goodcar.getProductImage());
            orderDetail.setProductName(goodcar.getProductName());
            orderDetailService.save(orderDetail);

        }


        return id;
    }

    /**
     * 修改订单信息
     *
     */
    @RequestMapping("/updateOrderStatus")
    @TokenAuth
    public void updateOrderStatus(@RequestParam String token, @RequestParam String orderId) {

        Orders order = orderService.getById(Long.valueOf(orderId));
        order.setOrderStatus(2L);
        orderService.saveOrUpdate(order);
    }

    /**
     * 付款
     */
    @RequestMapping("/goodcarUpdateTotalCoin")
    @TokenAuth
    public int goodcarUpdateTotalCoin(@RequestParam String token, @RequestParam String totalPrice,
                               @RequestParam String productOrderDetail) {

        //解析成List
        List<Goodcar> goodcars = JSON.parseArray(productOrderDetail, Goodcar.class);

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper coinTotalWrapper = new QueryWrapper();
        coinTotalWrapper.eq("open_id", openId);
        CoinTotal coinTotal = (CoinTotal) coinTotalService.list(coinTotalWrapper).get(0);
        Long coins = coinTotal.getCoinTotal();
        Long coin = coins - Long.valueOf(totalPrice);
        if(coin < 0) {
            return 0;
        }else {
            coinTotal.setCoinTotal(coin);
            coinTotalService.saveOrUpdate(coinTotal);

            /**
             * 添加运动币详情
             *
             */
            productUpdateDetailCoin(openId, coins);

            for (int i=0; i<goodcars.size(); i++) {
                /**
                 * 修改商品的数量
                 */
                Goodcar goodcar = goodcars.get(i);
                Product product = productService.getById(Long.valueOf(goodcar.getProductId()));
                product.setProductCount(product.getProductCount() - Long.valueOf(goodcar.getProductCount()));
                productService.saveOrUpdate(product);

                QueryWrapper colorWrapper = new QueryWrapper();
                colorWrapper.eq("product_id", Long.valueOf(goodcar.getProductId()));
                colorWrapper.eq("product_color", goodcar.getProductColor());
                ProductColor productColor = (ProductColor) productColorService.list(colorWrapper).get(0);
                productColor.setProudctCount(productColor.getProudctCount() - Long.valueOf(goodcar.getProductCount()));
                productColorService.saveOrUpdate(productColor);

                QueryWrapper typeWrapper = new QueryWrapper();
                typeWrapper.eq("product_id", Long.valueOf(goodcar.getProductId()));
                typeWrapper.eq("product_type", goodcar.getProductType());
                ProductType productType = (ProductType) productTypeService.list(typeWrapper).get(0);
                productType.setProductCount(productType.getProductCount() - Long.valueOf(goodcar.getProductCount()));
                productTypeService.saveOrUpdate(productType);
            }
            return 1;
        }
    }

    /**
     * 付款
     */
    @RequestMapping("/productUpdateTotalCoin")
    @TokenAuth
    public int productUpdateTotalCoin(@RequestParam String token, @RequestParam String totalPrice,
                                      @RequestParam String productId, @RequestParam String color,
                                      @RequestParam String type, @RequestParam String count
                                      ) {

        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        QueryWrapper coinTotalWrapper = new QueryWrapper();
        coinTotalWrapper.eq("open_id", openId);
        CoinTotal coinTotal = (CoinTotal) coinTotalService.list(coinTotalWrapper).get(0);
        Long coins = coinTotal.getCoinTotal();
        Long coin = coins - Long.valueOf(totalPrice);
        if(coin < 0) {
            return 0;
        }else {
            coinTotal.setCoinTotal(coin);
            coinTotalService.saveOrUpdate(coinTotal);

            /**
             * 添加运动币详情
             *
             */
            productUpdateDetailCoin(openId, coins);

            /**
             * 修改商品的数量
             */
            Product product = productService.getById(Long.valueOf(productId));
            product.setProductCount(product.getProductCount() - Long.valueOf(count));
            productService.saveOrUpdate(product);

            QueryWrapper colorWrapper = new QueryWrapper();
            colorWrapper.eq("product_id", Long.valueOf(productId));
            colorWrapper.eq("product_color", color);
            ProductColor productColor = (ProductColor) productColorService.list(colorWrapper).get(0);
            productColor.setProudctCount(productColor.getProudctCount() - Long.valueOf(count));
            productColorService.saveOrUpdate(productColor);

            QueryWrapper typeWrapper = new QueryWrapper();
            typeWrapper.eq("product_id", Long.valueOf(productId));
            typeWrapper.eq("product_type", type);
            ProductType productType = (ProductType) productTypeService.list(typeWrapper).get(0);
            productType.setProductCount(productType.getProductCount() - Long.valueOf(count));
            productTypeService.saveOrUpdate(productType);
            return 1;
        }
    }

    public void productUpdateDetailCoin(String openId, long coin) {
        String userInfoName = getUserInfoName(openId);
        CoinDetail coinDetail = new CoinDetail();
        coinDetail.setUserName(userInfoName)
                .setOpenId(openId)
                .setCreateTime(new Date().getTime())
                .setModifyTime(new Date().getTime())
                .setBeizhu("支付")
                .setCoin(0 - coin);

        coinDetailService.save(coinDetail);

    }
    public String getUserInfoName(String openId) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = userService.list(userQueryWrapper).get(0);
        return user.getUserName();
    }
    /**
     * 获取订单信息
     */
    @RequestMapping("/allOrderInfo")
    @TokenAuth
    public Object[] allOrderInfo(@RequestParam String token, @RequestParam String status) {

        List<Orders> orderlist;
        TokenInfo tokenInfo = (TokenInfo) TokenInfoMap.tokenMap.get(token);
        String openId = tokenInfo.getOpen_id();
        if(Integer.valueOf(status) == 0) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("open_id", openId);
            orderlist  = orderService.list(queryWrapper);
        }else {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("order_status", Long.valueOf(status));
            wrapper.eq("open_id", openId);
            orderlist  = orderService.list(wrapper);
        }
        Object[] orderArr = new Object[orderlist.size()];
        for (int i=0; i<orderlist.size(); i++) {
            Orders orders = orderlist.get(i);
            Long id = orders.getId();
            QueryWrapper orderDetailWrapper = new QueryWrapper();
            orderDetailWrapper.eq("order_id", id);
            List orderDetailList = orderDetailService.list(orderDetailWrapper);
            List allInfoOrderList = new ArrayList();

            allInfoOrderList.add(orders.getOrderPrice());
            allInfoOrderList.add(orderDetailList);
           orderArr[i] = allInfoOrderList;
        }

        return orderArr;
    }
}

