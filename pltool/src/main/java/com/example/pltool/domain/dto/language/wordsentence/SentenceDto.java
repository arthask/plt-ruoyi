package com.example.pltool.domain.dto.language.wordsentence;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SentenceDto {
  private String uuid;
  /**
   * 例句
   */
  private String sentenceContent;

  /**
   * 翻译内容
   */
  private String translateContent;

  /**
   * 创建时间
   */
  private LocalDateTime createTime;
}
