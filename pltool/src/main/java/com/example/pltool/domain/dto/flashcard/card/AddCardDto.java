package com.example.pltool.domain.dto.flashcard.card;

import lombok.Data;

@Data
public class AddCardDto {
  /**
   * 卡片类型
   */
  private Integer type;
  /**
   * 单词UUID
   */
  private String wordUUID;
  /**
   * 问题UUID
   */
  private String questionUUID;
  /**
   * 卡包UUID
   */
  private String packageUUID;
}
