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
 * @since 2019-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContentPraise implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 点赞表主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论表user_content的id
     */
    private Long contentId;

    /**
     * 点赞人的open_id
     */
    private String praiseId;

    /**
     * 点赞人的头像
     */
    private String userImage;

    /**
     * 点赞人的呢称
     */
    private String userName;

    /**
     * 是否点赞，0-取消点赞，1-已点赞
     */
    @TableField("isPraise")
    private Integer isPraise;

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
