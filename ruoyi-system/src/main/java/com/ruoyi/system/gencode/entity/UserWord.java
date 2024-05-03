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
 * 用户单词表
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user_word")
public class UserWord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单词id
     */
    @TableField("word_id")
    private Long wordId;

    /**
     * 单词
     */
    @TableField("word")
    private String word;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 用户名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 阶段，拥有标识单词的单词的熟悉程度
     */
    @TableField("period")
    private Integer period;

    /**
     * 是否收藏
     */
    @TableField("collect_flag")
    private Integer collectFlag;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 下次学习时间
     */
    @TableField("next_study_time")
    private LocalDateTime nextStudyTime;
}
