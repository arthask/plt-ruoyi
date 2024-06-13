package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.GetReplyRequest;
import com.ruoyi.system.domain.dto.scene.SceneData;
import com.ruoyi.system.gencode.entity.DialogueScene;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 对话场景表 服务类
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
public interface DialogueSceneService extends IService<DialogueScene> {
    /**
     * 新增对话场景
     *
     * @param sceneData 场景数据
     * @return
     */
    Map<String, Object> addDialogueScene(SceneData sceneData);

    /**
     * 更新场景对话
     * @param sceneData
     * @return
     */
    Boolean updateDialogueScene(SceneData sceneData);

    /**
     * 获取场景详对话详情
     *
     * @param sceneUUID 请求参数
     * @return
     */
    SceneData getDialogueSceneInfo(String sceneUUID);
}
