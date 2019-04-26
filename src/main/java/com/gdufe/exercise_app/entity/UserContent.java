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
 * @since 2019-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserContent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id,可拿到用户的头像和昵称
     */
    private String openId;

    /**
     * 用户头像
     */
    private String userImage;

    /**
     * 发表内容的用户昵称
     */
    private String userName;

    /**
     * 用户发表的图片，可存多张，通过,分隔
     */
    private String contentImage;

    /**
     * 用户发表的内容
     */
    private String contentContext;

    /**
     * 点赞图标
     */
    private String praiseIcon;

    /**
     * 点赞数
     */
    private Integer praiseData;

    /**
     * 0-所有人可见，1-仅自己可见
     */
    private Integer onlyMe;

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
