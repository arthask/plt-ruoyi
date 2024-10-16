package com.example.pltool.domain.dto.flashcard.card;

import java.util.List;

import lombok.Data;

@Data
public class CancelAssociationDto {
  private Long userId;
  private String cardUUId;
  private List<String> packageUUIdList;
}
