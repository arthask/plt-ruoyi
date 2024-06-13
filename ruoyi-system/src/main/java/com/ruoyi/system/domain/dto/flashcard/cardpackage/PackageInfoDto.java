package com.ruoyi.system.domain.dto.flashcard.cardpackage;

import com.ruoyi.system.domain.dto.label.LabelInfo;

import java.util.List;

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

    public String getUuid() {
        return uuid;
    }

    public PackageInfoDto setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public PackageInfoDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public PackageInfoDto setType(Integer type) {
        this.type = type;
        return this;
    }

    public List<LabelInfo> getLabelInfos() {
        return labelInfos;
    }

    public PackageInfoDto setLabelInfos(List<LabelInfo> labelInfos) {
        this.labelInfos = labelInfos;
        return this;
    }

    public Long getCardCount() {
        return cardCount;
    }

    public PackageInfoDto setCardCount(Long cardCount) {
        this.cardCount = cardCount;
        return this;
    }
}
