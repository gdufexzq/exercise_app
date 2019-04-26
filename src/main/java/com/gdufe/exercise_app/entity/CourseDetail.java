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
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程详细内容id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程id
     */
    @TableField("courseInfo_id")
    private Long courseinfoId;

    /**
     * 图片url
     */
    @TableField("imageUrl")
    private String imageUrl;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程描述
     */
    private String courseContext;

    /**
     * 课程等级
     */
    private String courseLevel;

    /**
     * 参加该课程的人数
     */
    private Integer personCount;

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
