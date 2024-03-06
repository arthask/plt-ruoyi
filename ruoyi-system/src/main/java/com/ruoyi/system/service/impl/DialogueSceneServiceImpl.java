package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Dialogue;
import com.ruoyi.system.domain.DialogueScene;
import com.ruoyi.system.domain.dto.GetReplyRequest;
import com.ruoyi.system.domain.dto.SceneData;
import com.ruoyi.system.mapper.DialogueMapper;
import com.ruoyi.system.mapper.DialogueSceneMapper;
import com.ruoyi.system.service.IDialogueSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.ruoyi.common.constant.Constants.IS_SUCCESS;
import static com.ruoyi.common.constant.Constants.NOTE_UUID;

/**
 * 对话场景Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-02-21
 */
@Service
public class DialogueSceneServiceImpl implements IDialogueSceneService 
{
    @Autowired
    private DialogueSceneMapper dialogueSceneMapper;

    @Autowired
    private DialogueMapper dialogueMapper;

    /**
     * 查询对话场景
     * 
     * @param id 对话场景主键
     * @return 对话场景
     */
    @Override
    public DialogueScene selectDialogueSceneById(Long id)
    {
        return dialogueSceneMapper.selectDialogueSceneById(id);
    }

    /**
     * 查询对话场景列表
     * 
     * @param dialogueScene 对话场景
     * @return 对话场景
     */
    @Override
    public List<DialogueScene> selectDialogueSceneList(DialogueScene dialogueScene)
    {
        return dialogueSceneMapper.selectDialogueSceneList(dialogueScene);
    }

    /**
     * 新增对话场景
     * 
     * @param dialogueScene 对话场景
     * @return 结果
     */
    @Override
    public int insertDialogueScene(DialogueScene dialogueScene)
    {
        dialogueScene.setCreateTime(DateUtils.getNowDate());
        return dialogueSceneMapper.insertDialogueScene(dialogueScene);
    }

    /**
     * 修改对话场景
     * 
     * @param dialogueScene 对话场景
     * @return 结果
     */
    @Override
    public int updateDialogueScene(DialogueScene dialogueScene)
    {
        dialogueScene.setUpdateTime(DateUtils.getNowDate());
        return dialogueSceneMapper.updateDialogueScene(dialogueScene);
    }

    /**
     * 批量删除对话场景
     * 
     * @param ids 需要删除的对话场景主键
     * @return 结果
     */
    @Override
    public int deleteDialogueSceneByIds(Long[] ids)
    {
        return dialogueSceneMapper.deleteDialogueSceneByIds(ids);
    }

    /**
     * 删除对话场景信息
     * 
     * @param id 对话场景主键
     * @return 结果
     */
    @Override
    public int deleteDialogueSceneById(Long id)
    {
        return dialogueSceneMapper.deleteDialogueSceneById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addDialogueScene(SceneData sceneData) {
        if (StringUtils.isEmpty(sceneData.getName())
                || CollectionUtil.isEmpty(sceneData.getDialogueDataList())) {
            throw new IllegalArgumentException("必填参数为空");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(IS_SUCCESS, false);
        // 先插入场景，再新增对话
        DialogueScene dialogueScene = new DialogueScene();
        dialogueScene.setName(sceneData.getName());
        // todo 先写为默认值
        dialogueScene.setTagId(1L);
        dialogueScene.setUserId(sceneData.getUserId());
        String uuid = UUID.randomUUID().toString();
        dialogueScene.setUuid(uuid);
        int affectRow = dialogueSceneMapper.insertDialogueScene(dialogueScene);
        if (affectRow > 0) {
            DialogueScene scene = dialogueSceneMapper.selectSceneByUUId(uuid);
            List<Dialogue> addDataList = new ArrayList<>();
            sceneData.getDialogueDataList().forEach(e -> {
                Dialogue dialogue = new Dialogue();
                dialogue.setUuid(UUID.randomUUID().toString());
                dialogue.setSceneId(scene.getId());
                dialogue.setSenderContent(e.getSenderContent());
                dialogue.setReply(e.getReply());
                dialogue.setSortNum(e.getSortNum());
                dialogue.setCreateUserId(sceneData.getUserId());
                addDataList.add(dialogue);
            });
            if (!CollectionUtil.isEmpty(addDataList)) {
                resultMap.put(IS_SUCCESS, dialogueMapper.insertDialogueBatch(addDataList) > 0);
                resultMap.put(NOTE_UUID, uuid);
                return resultMap;
            }
        }
        return resultMap;
    }

    @Override
    public String getReplayInfo(GetReplyRequest getReplyRequest) {
        if (StringUtils.isEmpty(getReplyRequest.getSceneUUId())
                || Objects.isNull(getReplyRequest.getSortNum())) {
            throw new IllegalArgumentException("请求参数为空");
        }
        // 使用场景的uuid和sort查询对话内容
        DialogueScene dialogueScene = dialogueSceneMapper.selectSceneByUUId(getReplyRequest.getSceneUUId());
        if (Objects.isNull(dialogueScene)) {
            throw new RuntimeException("未找到场景");
        }
        Long sceneId = dialogueScene.getId();
        Dialogue query = new Dialogue();
        query.setSceneId(sceneId);
        query.setSortNum(Long.valueOf(getReplyRequest.getSortNum()));
        // 只会查出一个
        List<Dialogue> dialogues = dialogueMapper.selectDialogueList(query);
        if (CollectionUtil.isEmpty(dialogues)) {
            return "conversation is end";
        }
        Dialogue dialogue = dialogues.get(0);
        return dialogue.getSenderContent();
    }
}
