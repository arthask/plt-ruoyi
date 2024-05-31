package com.ruoyi.system.domain.dto.flashcard.card;


public class AddCardDto {
    /**
     * 卡片类型
     */
    private Integer type;
    /**
     * 单词UUID
     */
    private String wordUUID;
    /**
     * 卡包UUID
     */
    private String packageUUID;

    public Integer getType() {
        return type;
    }

    public AddCardDto setType(Integer type) {
        this.type = type;
        return this;
    }

    public String getWordUUID() {
        return wordUUID;
    }

    public AddCardDto setWordUUID(String wordUUID) {
        this.wordUUID = wordUUID;
        return this;
    }

    public String getPackageUUID() {
        return packageUUID;
    }

    public AddCardDto setPackageUUID(String packageUUID) {
        this.packageUUID = packageUUID;
        return this;
    }
}
