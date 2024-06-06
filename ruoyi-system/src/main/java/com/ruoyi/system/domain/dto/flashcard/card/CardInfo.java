package com.ruoyi.system.domain.dto.flashcard.card;

import com.ruoyi.system.domain.dto.flashcard.cardpackage.PackageInfoDto;

public class CardInfo {
    private String front;
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
}
