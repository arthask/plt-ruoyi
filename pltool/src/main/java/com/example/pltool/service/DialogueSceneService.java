package com.example.pltool.service;


import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.scene.SceneData;
import com.example.pltool.domain.entity.DialogueScene;

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
   * 
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

  /**
   * 删除场景对话
   * 
   * @param uuids
   * @return
   */
  String removeScene(String[] uuids);
}
