package com.example.pltool.domain.dto.flashcard.card;


import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;

public class CardInfo {
    private Long cardCount;
    private String uuid;
    private String front;
    private String back;
    private Integer type;
    private PackageInfoDto packageInfoDto;

    public String getFront() {
        return front;
    }

    public CardInfo setFront(String front) {
        this.front = front;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public CardInfo setType(Integer type) {
        this.type = type;
        return this;
    }

    public PackageInfoDto getPackageInfoDto() {
        return packageInfoDto;
    }

    public CardInfo setPackageInfoDto(PackageInfoDto packageInfoDto) {
        this.packageInfoDto = packageInfoDto;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public CardInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getBack() {
        return back;
    }

    public CardInfo setBack(String back) {
        this.back = back;
        return this;
    }
}
