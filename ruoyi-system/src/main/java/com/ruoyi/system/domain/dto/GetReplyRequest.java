package com.ruoyi.system.domain.dto;

public class GetReplyRequest {
    private String sceneUUId;
    private Integer sortNum;

    public String getSceneUUId() {
        return sceneUUId;
    }

    public void setSceneUUId(String sceneUUId) {
        this.sceneUUId = sceneUUId;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
}
