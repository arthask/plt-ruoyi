package com.example.pltool.controller.business.constant.enums;

public enum NoteTypeEnum {
    QUESTION(0, "问题笔记"),
    WORD(1, "单词笔记");
    private final Integer value;
    private final String desc;

    NoteTypeEnum(Integer value, String desc) {
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
