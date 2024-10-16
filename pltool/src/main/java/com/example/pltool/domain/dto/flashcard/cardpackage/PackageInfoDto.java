package com.example.pltool.domain.dto.flashcard.cardpackage;


import java.util.List;

import com.example.pltool.domain.dto.label.LabelInfo;

import lombok.Data;

@Data
public class PackageInfoDto {
  /**
   * uuid
   */
  private String uuid;
  /**
   * 卡包名
   */
  private String name;
  /**
   * 卡包中的卡片数量
   */
  private Long cardCount;
  /**
   * 卡包类型
   */
  private Integer type;
  /**
   * 卡片标签
   */
  private List<LabelInfo> labelInfos;
}
