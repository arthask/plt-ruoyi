package com.ruoyi.system.domain.dto.flashcard.cardattribute;

public class ClassifyCountVo {
    private String name;
    private Integer familiarity;
    private Long count;

    public Integer getFamiliarity() {
        return familiarity;
    }

    public ClassifyCountVo setFamiliarity(Integer familiarity) {
        this.familiarity = familiarity;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public ClassifyCountVo setCount(Long count) {
        this.count = count;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClassifyCountVo setName(String name) {
        this.name = name;
        return this;
    }

    public void setNameByFamiliarity(int familiarity) {
        if (familiarity == 0) {
            this.name = "不会";
        } else if (familiarity == 1) {
            this.name = "会";
        } else if (familiarity == 2) {
            name = "待定";
        }
    }
}
