package com.example.pltool.domain.dto.scene;

import lombok.Data;

import java.util.List;

@Data
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

    private String introduce;

    private String studyInfo;

    private String summary;
}
