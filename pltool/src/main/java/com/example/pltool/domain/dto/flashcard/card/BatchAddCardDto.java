package com.example.pltool.domain.dto.flashcard.card;

import java.util.List;

import lombok.Data;

@Data
public class BatchAddCardDto {
  private Long userId;
  private Integer cardType;
  private List<String> uuidList;
  private List<CardContent> cardContents;
}
