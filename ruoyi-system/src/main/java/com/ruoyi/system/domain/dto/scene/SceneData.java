package com.ruoyi.system.domain.dto.scene;

import java.util.List;

public class SceneData {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 名称
     */
    private String name;

    private List<DialogueData> dialogueDataList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUuid() {
        return uuid;
    }

    public SceneData setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
