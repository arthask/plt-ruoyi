package com.ruoyi.system.domain.dto;

import java.util.List;

public class SceneData {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 名称
     */
    private String name;

    /**
     * tag id
     */
    private Long tagId;

    private List<DialogueData> dialogueDataList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public List<DialogueData> getDialogueDataList() {
        return dialogueDataList;
    }

    public void setDialogueDataList(List<DialogueData> dialogueDataList) {
        this.dialogueDataList = dialogueDataList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
