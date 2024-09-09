package com.example.pltool.domain.dto.flashcard.card;

import lombok.Data;

import java.util.List;

@Data
public class BatchAddCardDto {
    private Long userId;
    private Integer cardType;
    private List<String> uuidList;
    private List<CardContent> cardContents;
}
