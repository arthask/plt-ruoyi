package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Dialogue;

import java.util.List;

/**
 * 对话Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-21
 */
public interface DialogueMapper 
{
    /**
     * 查询对话
     * 
     * @param id 对话主键
     * @return 对话
     */
    public Dialogue selectDialogueById(Long id);

    /**
     * 查询对话列表
     * 
     * @param dialogue 对话
     * @return 对话集合
     */
    public List<Dialogue> selectDialogueList(Dialogue dialogue);

    /**
     * 新增对话
     * 
     * @param dialogue 对话
     * @return 结果
     */
    public int insertDialogue(Dialogue dialogue);

    /**
     * 修改对话
     * 
     * @param dialogue 对话
     * @return 结果
     */
    public int updateDialogue(Dialogue dialogue);

    /**
     * 删除对话
     * 
     * @param id 对话主键
     * @return 结果
     */
    public int deleteDialogueById(Long id);

    /**
     * 批量删除对话
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDialogueByIds(Long[] ids);

    /**
     * 批量新增会话
     *
     * @param dialogues 会话列表
     * @return 结果
     */
    int insertDialogueBatch(List<Dialogue> dialogues);
}
