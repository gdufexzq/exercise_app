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
public class ContentComment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论表主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * user_content表的id
     */
    private Long contentId;

    /**
     * 评论人的openId
     */
    private String userId;

    /**
     * 评论用户的用户头像
     */
    private String userImage;

    /**
     * 评论用户的昵称
     */
    private String userName;

    /**
     * 评论内容
     */
    private String commentContent;

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
