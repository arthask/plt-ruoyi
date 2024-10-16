package com.example.pltool.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 词库与单词关系表
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("lexicon_word")
public class LexiconWord implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * uuid
   */
  @TableField("uuid")
  private String uuid;

  /**
   * 词库id
   */
  @TableField("lexicon_uuid")
  private String lexiconUuid;

  /**
   * 单词id
   */
  @TableField("word_uuid")
  private String wordUuid;

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
