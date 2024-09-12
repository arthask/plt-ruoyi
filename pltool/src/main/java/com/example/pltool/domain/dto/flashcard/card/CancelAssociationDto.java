package com.example.pltool.domain.dto.flashcard.card;

import lombok.Data;

import java.util.List;

@Data
public class CancelAssociationDto {
    private Long userId;
    private String cardUUId;
    private List<String> packageUUIdList;
}
