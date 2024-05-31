package com.ruoyi.system.gencode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 闪卡表
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("flashcard")
public class Flashcard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 记录uuid
     */
    @TableField("uuid")
    private String uuid;

    /**
     * front内容
     */
    @TableField("front")
    private String front;

    /**
     * back内容
     */
    @TableField("back")
    private String back;

    /**
     * 卡片类型
     */
    @TableField("type")
    private Integer type;

    @TableField("user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
