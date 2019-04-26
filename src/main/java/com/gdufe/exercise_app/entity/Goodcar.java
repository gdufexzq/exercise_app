package com.gdufe.exercise_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author xuzhiquan
 * @since 2019-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Goodcar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品数量
     */
    private Long productCount;

    /**
     * 商品图像
     */
    private String productImage;

    /**
     * 商品价格
     */
    private Long productPrice;

    /**
     * 商品颜色
     */
    private String productColor;

    /**
     * 商品类型
     */
    private String productType;

    /**
     * 所属用户
     */
    private String openId;

    /**
     * 用于购物车中判断商品是否被选中,0-没选中，1-选中
     */
    private Integer ischoose;

    /**
     * 判断购物车的商品是否已经变成订单了,0-没买，1-已买，
     */
    @TableField("isOrder")
    private Integer isOrder;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 扩展字段1
     */
    private String ext1;

    /**
     * 扩展字段2
     */
    private String ext2;

    /**
     * 扩展字段3
     */
    private String ext3;


}
