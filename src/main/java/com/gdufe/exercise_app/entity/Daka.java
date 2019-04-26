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
 * @since 2019-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Daka implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 当前用户名称
     */
    private String userName;

    /**
     * 当前日期
     */
    private Long curTime;

    /**
     * 是否打卡，0-没打卡，1-打卡
     */
    private Integer daka;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
    private Long modifyTime;

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 扩展字段1
     */
    private String ex1;

    /**
     * 扩展字段2
     */
    private String ex2;

    /**
     * 扩展字段3
     */
    private String ex3;


}
