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
 * @since 2019-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属用户
     */
    private String openId;

    /**
     * 外键，订单地址
     */
    private Long addressId;

    /**
     * 订单总价格
     */
    private Long orderPrice;

    /**
     * 0-表示查询全部,1-待支付，2-已支付未发货，3-已发货未签收，4-已签收
     */
    private Long orderStatus;

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
