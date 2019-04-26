package com.gdufe.exercise_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品数量
     */
    private Long productCount;

    /**
     * 商品价格
     */
    private Long productPrice;

    /**
     * 商品颜色
     */
    private String productColor;

    /**
     * 商品规格
     */
    private String productType;

    /**
     * 所属订单
     */
    private Long orderId;

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
    private Long ext1;

    /**
     * 扩展字段2
     */
    private Long ext2;

    /**
     * 扩展字段3
     */
    private Long ext3;

    /**
     * 商品头像
     */
    private String productImage;


}
