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
 * @since 2019-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CourseActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程动作id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程详细信息id
     */
    @TableField("courseDetail_id")
    private Long coursedetailId;

    /**
     * 图片url
     */
    @TableField("imageUrl")
    private String imageUrl;

    /**
     * 动作名称
     */
    private String activityName;

    /**
     * 动作组数
     */
    private String activityPayload;

    /**
     * 动作视频的url
     */
    @TableField("videoUrl")
    private String videoUrl;

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
