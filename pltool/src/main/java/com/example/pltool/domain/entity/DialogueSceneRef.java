package com.example.pltool.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 场景对话关系表
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dialogue_scene_ref")
public class DialogueSceneRef implements Serializable {

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
     * 对话uuid
     */
    @TableField("dialogue_uuid")
    private String dialogueUuid;

    /**
     * 场景uuid
     */
    @TableField("scene_uuid")
    private String sceneUuid;

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
