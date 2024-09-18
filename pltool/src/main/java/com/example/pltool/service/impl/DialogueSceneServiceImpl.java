package com.example.pltool.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.scene.DialogueData;
import com.example.pltool.domain.dto.scene.SceneData;
import com.example.pltool.domain.entity.Dialogue;
import com.example.pltool.domain.entity.DialogueScene;
import com.example.pltool.domain.entity.DialogueSceneRef;
import com.example.pltool.mapper.DialogueSceneMapper;
import com.example.pltool.service.DialogueSceneRefService;
import com.example.pltool.service.DialogueSceneService;
import com.example.pltool.service.DialogueService;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruoyi.common.constant.Constants.IS_SUCCESS;
import static com.ruoyi.common.constant.Constants.NOTE_UUID;

/**
 * <p>
 * 对话场景表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@Service
public class DialogueSceneServiceImpl extends ServiceImpl<DialogueSceneMapper, DialogueScene> implements DialogueSceneService {

    @Autowired
    private DialogueService dialogueService;

    @Autowired
    private DialogueSceneMapper dialogueSceneMapper;

    @Autowired
    private DialogueSceneRefService dialogueSceneRefService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> addDialogueScene(SceneData sceneData) {
        if (StringUtils.isEmpty(sceneData.getName())) {
            throw new IllegalArgumentException("必填参数为空");
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(IS_SUCCESS, false);
        // 先插入场景，再新增对话
        DialogueScene dialogueScene = new DialogueScene();
        dialogueScene.setName(sceneData.getName());
        if (StringUtils.isNotBlank(sceneData.getIntroduce())) {
            dialogueScene.setIntroduce(sceneData.getIntroduce());
        }
        if (StringUtils.isNotBlank(sceneData.getStudyInfo())) {
            dialogueScene.setStudyInfo(sceneData.getStudyInfo());
        }
        dialogueScene.setUserId(sceneData.getUserId());
        String sceneUUID = UUID.randomUUID().toString().replace("-", "");
        dialogueScene.setUuid(sceneUUID);
        int affectRow = getBaseMapper().insert(dialogueScene);
        if (affectRow > 0) {
            if (CollectionUtils.isEmpty(sceneData.getDialogueDataList())) {
                resultMap.put(IS_SUCCESS, Boolean.TRUE);
                resultMap.put(NOTE_UUID, sceneUUID);
                return resultMap;
            }
            List<Dialogue> addDataList = new ArrayList<>();
            List<DialogueSceneRef> dialogueSceneRefList = new ArrayList<>();
            sceneData.getDialogueDataList().forEach(e -> {
                String dialogueUUID = UUID.randomUUID().toString().replace("-", "");
                Dialogue dialogue = new Dialogue();
                dialogue.setUuid(dialogueUUID);
                dialogue.setSenderContent(e.getSenderContent());
                dialogue.setReply(e.getReply());
                dialogue.setSortNum(e.getSortNum());
                dialogue.setCreateUserId(sceneData.getUserId());
                addDataList.add(dialogue);
                String refUUID = UUID.randomUUID().toString().replace("-", "");
                DialogueSceneRef dialogueSceneRef = new DialogueSceneRef();
                dialogueSceneRef.setDialogueUuid(dialogueUUID);
                dialogueSceneRef.setSceneUuid(sceneUUID);
                dialogueSceneRef.setUuid(refUUID);
                dialogueSceneRefList.add(dialogueSceneRef);
            });
            if (!CollectionUtil.isEmpty(addDataList)) {
                dialogueService.saveBatch(addDataList);
            }
            if (!CollectionUtil.isEmpty(dialogueSceneRefList)) {
                resultMap.put(IS_SUCCESS, dialogueSceneRefService.saveBatch(dialogueSceneRefList));
                resultMap.put(NOTE_UUID, sceneUUID);
                return resultMap;
            }
        }
        return resultMap;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateDialogueScene(SceneData sceneData) {
        DialogueScene dialogueSceneFromBb = getDialogueSceneByUUID(sceneData.getUuid());
        boolean updateSceneFlag = false;
        DialogueScene updateDialogueScene = new DialogueScene();
        // 修改场景名称
        if (StringUtils.isNoneBlank(sceneData.getName())
                && !dialogueSceneFromBb.getName().equals(sceneData.getName())) {
            updateDialogueScene.setName(sceneData.getName());
            updateSceneFlag = true;
        }
        if (StringUtils.isNotBlank(sceneData.getIntroduce())) {
            updateDialogueScene.setIntroduce(sceneData.getIntroduce());
            updateSceneFlag = true;
        }
        if (StringUtils.isNotBlank(sceneData.getStudyInfo())) {
            updateDialogueScene.setStudyInfo(sceneData.getStudyInfo());
            updateSceneFlag = true;
        }
        if (StringUtils.isNotBlank(sceneData.getSummary())) {
            updateDialogueScene.setSummary(sceneData.getSummary());
            updateSceneFlag = true;
        }
        if (updateSceneFlag) {
            updateDialogueScene.setUpdateTime(LocalDateTime.now());
            updateDialogueScene.setId(dialogueSceneFromBb.getId());
            updateById(updateDialogueScene);
        }
        List<DialogueData> dialogueDataList = sceneData.getDialogueDataList();
        Map<String, DialogueData> dialogueDataMap = dialogueDataList.stream()
                .filter(e -> org.apache.commons.lang3.StringUtils.isNotBlank(e.getUuid()))
                .collect(Collectors.toMap(DialogueData::getUuid, Function.identity(), (k1, k2) -> k2));

        List<DialogueData> dialoguesOfScene = dialogueSceneMapper.getDialoguesOfScene(sceneData.getUuid());
        Map<String, DialogueData> dialogueDataMapFromDb = dialoguesOfScene.stream()
                .collect(Collectors.toMap(DialogueData::getUuid, Function.identity(), (k1, k2) -> k2));
        List<Dialogue> updateDialogues = new ArrayList<>();

        List<Dialogue> deleteDialogues = new ArrayList<>();
        // 对比对话内容是否有修改
        dialogueDataMapFromDb.forEach((k, v) -> {
            if (dialogueDataMap.containsKey(k)) {
                DialogueData dialogueData = dialogueDataMap.get(k);
                Dialogue updateDialogue = new Dialogue();
                boolean needUpdateFlag = false;
                if (!Objects.equals(v.getSenderContent(), dialogueData.getSenderContent())) {
                    updateDialogue.setSenderContent(dialogueData.getSenderContent());
                    needUpdateFlag = true;
                }
                if (!Objects.equals(v.getReply(), dialogueData.getReply())) {
                    updateDialogue.setReply(dialogueData.getReply());
                    needUpdateFlag = true;
                }
                if (needUpdateFlag) {
                    updateDialogue.setId(v.getId());
                    updateDialogues.add(updateDialogue);
                }
            } else {
                Dialogue deleteDialogue = new Dialogue();
                deleteDialogue.setId(v.getId());
                deleteDialogue.setUuid(v.getUuid());
                // 需要删除的对话数据
                deleteDialogues.add(deleteDialogue);
                // 需要删除的关联数据

            }
        });
        List<Dialogue> addDataList = new ArrayList<>();
        List<DialogueSceneRef> addDialogueSceneRefList = new ArrayList<>();
        // 找到新增的数据
        dialogueDataList.stream()
                .filter(e -> org.apache.commons.lang3.StringUtils.isBlank(e.getUuid()))
                .forEach(e -> {
                    Dialogue addDialogue = new Dialogue();
                    String dialogueUUID = UUID.randomUUID().toString().replace("-", "");
                    addDialogue.setUuid(dialogueUUID);
                    addDialogue.setSenderContent(e.getSenderContent());
                    addDialogue.setReply(e.getReply());
                    addDialogue.setSortNum(e.getSortNum());
                    addDialogue.setCreateUserId(sceneData.getUserId());
                    addDataList.add(addDialogue);
                    String refUUID = UUID.randomUUID().toString().replace("-", "");
                    ;
                    DialogueSceneRef dialogueSceneRef = new DialogueSceneRef();
                    dialogueSceneRef.setDialogueUuid(dialogueUUID);
                    dialogueSceneRef.setSceneUuid(dialogueSceneFromBb.getUuid());
                    dialogueSceneRef.setUuid(refUUID);
                    addDialogueSceneRefList.add(dialogueSceneRef);
                });
        if (!CollectionUtils.isEmpty(updateDialogues)) {
            dialogueService.updateBatchById(updateDialogues);
        }
        if (!CollectionUtils.isEmpty(deleteDialogues)) {
            dialogueService.removeBatchByIds(deleteDialogues);
            List<String> dialoguesUUIDList = deleteDialogues.stream().map(Dialogue::getUuid).collect(Collectors.toList());
            QueryWrapper<DialogueSceneRef> deleteQuery = new QueryWrapper<>();
            deleteQuery.in("dialogue_uuid", dialoguesUUIDList);
            deleteQuery.eq("scene_uuid", dialogueSceneFromBb.getUuid());
            dialogueSceneRefService.remove(deleteQuery);
        }
        if (!CollectionUtil.isEmpty(addDataList)) {
            dialogueService.saveBatch(addDataList);
        }
        if (!CollectionUtil.isEmpty(addDialogueSceneRefList)) {
            dialogueSceneRefService.saveBatch(addDialogueSceneRefList);
        }
        return Boolean.TRUE;
    }

    @Override
    public SceneData getDialogueSceneInfo(String sceneUUID) {
        DialogueScene dialogueScene = getDialogueSceneByUUID(sceneUUID);
        Assert.notNull(dialogueScene, "请求参数错误");
        SceneData sceneData = new SceneData();
        sceneData.setName(dialogueScene.getName());
        sceneData.setIntroduce(dialogueScene.getIntroduce());
        sceneData.setStudyInfo(dialogueScene.getStudyInfo());
        sceneData.setSummary(dialogueScene.getSummary());
        sceneData.setUuid(dialogueScene.getUuid());
        List<DialogueData> dialoguesOfScene = dialogueSceneMapper.getDialoguesOfScene(sceneUUID);
        if (!CollectionUtils.isEmpty(dialoguesOfScene)) {
            sceneData.setDialogueDataList(dialoguesOfScene);
        }
        return sceneData;
    }

    @Override
    public String removeScene(String[] uuids) {
        Assert.notEmpty(uuids, "请求参数错误");
        // 删除场景
        QueryWrapper<DialogueScene> dialogueSceneQueryWrapper = new QueryWrapper<>();
        dialogueSceneQueryWrapper.in("uuid", Arrays.asList(uuids));
        remove(dialogueSceneQueryWrapper);
        // 删除场景与对话的关联关系
        QueryWrapper<DialogueSceneRef> dialogueSceneRefQueryWrapper = new QueryWrapper<>();
        dialogueSceneRefQueryWrapper.in("scene_uuid", Arrays.asList(uuids));
        dialogueSceneRefService.remove(dialogueSceneRefQueryWrapper);
        return "删除成功";
    }

    private DialogueScene getDialogueSceneByUUID(String sceneUUID) {
        QueryWrapper<DialogueScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", sceneUUID);
        return getOne(queryWrapper);
    }

    private List<DialogueScene> getDialogueSceneByUUIDList(List<String> sceneUUIDList) {
        QueryWrapper<DialogueScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("uuid", sceneUUIDList);
        return list(queryWrapper);
    }
}
