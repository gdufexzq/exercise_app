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
 * @since 2019-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ExerciseTotal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 统计训练次数id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户的open_id
     */
    private String openId;

    /**
     * 用户的总运动次数
     */
    @TableField("exercise_totalCount")
    private Integer exerciseTotalcount;

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
