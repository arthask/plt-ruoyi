package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.DialogueMapper;
import com.ruoyi.system.domain.Dialogue;
import com.ruoyi.system.service.IDialogueService;

/**
 * 对话Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-02-21
 */
@Service
public class DialogueServiceImpl implements IDialogueService 
{
    @Autowired
    private DialogueMapper dialogueMapper;

    /**
     * 查询对话
     * 
     * @param id 对话主键
     * @return 对话
     */
    @Override
    public Dialogue selectDialogueById(Long id)
    {
        return dialogueMapper.selectDialogueById(id);
    }

    /**
     * 查询对话列表
     * 
     * @param dialogue 对话
     * @return 对话
     */
    @Override
    public List<Dialogue> selectDialogueList(Dialogue dialogue)
    {
        return dialogueMapper.selectDialogueList(dialogue);
    }

    /**
     * 新增对话
     * 
     * @param dialogue 对话
     * @return 结果
     */
    @Override
    public int insertDialogue(Dialogue dialogue)
    {
        dialogue.setCreateTime(DateUtils.getNowDate());
        return dialogueMapper.insertDialogue(dialogue);
    }

    /**
     * 修改对话
     * 
     * @param dialogue 对话
     * @return 结果
     */
    @Override
    public int updateDialogue(Dialogue dialogue)
    {
        dialogue.setUpdateTime(DateUtils.getNowDate());
        return dialogueMapper.updateDialogue(dialogue);
    }

    /**
     * 批量删除对话
     * 
     * @param ids 需要删除的对话主键
     * @return 结果
     */
    @Override
    public int deleteDialogueByIds(Long[] ids)
    {
        return dialogueMapper.deleteDialogueByIds(ids);
    }

    /**
     * 删除对话信息
     * 
     * @param id 对话主键
     * @return 结果
     */
    @Override
    public int deleteDialogueById(Long id)
    {
        return dialogueMapper.deleteDialogueById(id);
    }
}
