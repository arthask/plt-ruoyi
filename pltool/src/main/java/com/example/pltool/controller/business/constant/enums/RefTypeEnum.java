package com.example.pltool.controller.business.constant.enums;


public enum RefTypeEnum {
    LEXICON(1, "词库"),
    WORD(2, "单词"),
    WORD_COLLECTION_OF_PACKAGE(3,"卡包-单词集"),
    CARD_COLLECTION(4, "卡片-单词集");

    private final Integer value;
    private final String desc;

    RefTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
