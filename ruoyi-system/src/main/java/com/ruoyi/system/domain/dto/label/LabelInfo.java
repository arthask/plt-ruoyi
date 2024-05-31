package com.ruoyi.system.domain.dto.label;

public class LabelInfo {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 标签名称
     */
    private String name;

    public String getUuid() {
        return uuid;
    }

    public LabelInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public LabelInfo setName(String name) {
        this.name = name;
        return this;
    }
}
