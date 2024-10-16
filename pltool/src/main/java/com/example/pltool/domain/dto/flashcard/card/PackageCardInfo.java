package com.example.pltool.domain.dto.flashcard.card;

import java.util.List;

import lombok.Data;

@Data
public class PackageCardInfo {
  /**
   * 卡包uuid
   */
  private String packageUUId;
  /**
   * 卡片uuid集合
   */
  private List<String> cardUUIdList;

  private Long userId;
}
