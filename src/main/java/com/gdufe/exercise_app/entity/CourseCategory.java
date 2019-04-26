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
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 目录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图片url
     */
    @TableField("imageUrl")
    private String imageUrl;

    /**
     * 目录名称
     */
    private String categoryName;

    /**
     * 目录下课程的数量
     */
    private Integer categoryCount;

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
