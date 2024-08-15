package com.example.pltool.controller.business.constant.enums;

public enum TargetTypeEnum {
    LEXICON(1, "词库"),
    WORD(2, "单词");

    private final Integer value;
    private final String desc;

    TargetTypeEnum(Integer value, String desc) {
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
