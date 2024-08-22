package com.example.pltool.controller.business.constant.enums;


public enum CardTypeEnum {
    WORD(1, "单词"),
    QUESTION(2, "问题"),
    EXPRESSION(3, "表达");

    private final Integer value;
    private final String desc;

    CardTypeEnum(Integer value, String desc) {
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
