package com.example.pltool.domain.dto.flashcard.cardattribute;

public class StudyCardDto {
    private String cardUUID;
    private Integer familiarity;

    public String getCardUUID() {
        return cardUUID;
    }

    public StudyCardDto setCardUUID(String cardUUID) {
        this.cardUUID = cardUUID;
        return this;
    }

    public Integer getFamiliarity() {
        return familiarity;
    }

    public StudyCardDto setFamiliarity(Integer familiarity) {
        this.familiarity = familiarity;
        return this;
    }
}
