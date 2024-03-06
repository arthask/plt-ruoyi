package com.ruoyi.system.service;

import com.ruoyi.system.domain.DialogueScene;
import com.ruoyi.system.domain.dto.GetReplyRequest;
import com.ruoyi.system.domain.dto.SceneData;

import java.util.List;
import java.util.Map;

/**
 * 对话场景Service接口
 * 
 * @author ruoyi
 * @date 2024-02-21
 */
public interface IDialogueSceneService 
{
    /**
     * 查询对话场景
     * 
     * @param id 对话场景主键
     * @return 对话场景
     */
    public DialogueScene selectDialogueSceneById(Long id);

    /**
     * 查询对话场景列表
     * 
     * @param dialogueScene 对话场景
     * @return 对话场景集合
     */
    public List<DialogueScene> selectDialogueSceneList(DialogueScene dialogueScene);

    /**
     * 新增对话场景
     * 
     * @param dialogueScene 对话场景
     * @return 结果
     */
    public int insertDialogueScene(DialogueScene dialogueScene);

    /**
     * 修改对话场景
     * 
     * @param dialogueScene 对话场景
     * @return 结果
     */
    public int updateDialogueScene(DialogueScene dialogueScene);

    /**
     * 批量删除对话场景
     * 
     * @param ids 需要删除的对话场景主键集合
     * @return 结果
     */
    public int deleteDialogueSceneByIds(Long[] ids);

    /**
     * 删除对话场景信息
     * 
     * @param id 对话场景主键
     * @return 结果
     */
    public int deleteDialogueSceneById(Long id);

    /**
     * 新增对话场景
     * @param sceneData 场景数据
     * @return
     */
    Map<String, Object> addDialogueScene(SceneData sceneData);

    /**
     * 根据排序获取回答
     * @param getReplyRequest 请求参数
     * @return
     */
    String getReplayInfo(GetReplyRequest getReplyRequest);
}
