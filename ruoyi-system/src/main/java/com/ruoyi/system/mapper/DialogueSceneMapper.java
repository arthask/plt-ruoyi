package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.DialogueScene;

import java.util.List;

/**
 * 对话场景Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-21
 */
public interface DialogueSceneMapper 
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
     * 删除对话场景
     * 
     * @param id 对话场景主键
     * @return 结果
     */
    public int deleteDialogueSceneById(Long id);

    /**
     * 批量删除对话场景
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDialogueSceneByIds(Long[] ids);

    /**
     * 查询场景
     *
     * @param uuid 场景主键
     * @return 场景
     */
    DialogueScene selectSceneByUUId(String uuid);
}
