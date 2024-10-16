package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 表达记录表
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("expression")
public class Expression implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @JsonIgnore
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 记录uuid
   */
  @TableField("uuid")
  private String uuid;

  /**
   * 内容
   */
  @TableField("content")
  private String content;

  @TableField("user_id")
  private Long userId;

  /**
   * 创建时间
   */
  @TableField("create_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @TableField("update_time")
  private LocalDateTime updateTime;
}
