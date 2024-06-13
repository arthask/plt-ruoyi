package com.ruoyi.web.controller.business.scene;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.GetReplyRequest;
import com.ruoyi.system.domain.dto.scene.SceneData;
import com.ruoyi.system.gencode.entity.DialogueScene;
import com.ruoyi.system.gencode.service.DialogueSceneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 对话场景表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@RestController
@RequestMapping("/dialogueScene")
public class DialogueSceneController extends BaseController {
    @Autowired
    private DialogueSceneService dialogueSceneService;

    /**
     * 查询对话场景列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "name", required = false) String name) {
        startPage();
        QueryWrapper<DialogueScene> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        };
        queryWrapper.orderByDesc("create_time");
        List<DialogueScene> list = dialogueSceneService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 新增对话场景
     */
    @PostMapping("/addDialogueScene")
    public AjaxResult addDialogueScene(@RequestBody SceneData sceneData) {
        sceneData.setUserId(getUserId());
        return AjaxResult.success(dialogueSceneService.addDialogueScene(sceneData));
    }

    /**
     * 修改对话场景
     */
    @PostMapping("/updateDialogueScene")
    public AjaxResult updateDialogueScene(@RequestBody SceneData sceneData) {
        sceneData.setUserId(getUserId());
        return AjaxResult.success(dialogueSceneService.updateDialogueScene(sceneData));
    }

    /**
     * 修改对话场景
     */
    @GetMapping("/getDialogueSceneInfo")
    public AjaxResult getDialogueSceneInfo(@RequestParam("sceneUUID") String sceneUUID) {
        return AjaxResult.success(dialogueSceneService.getDialogueSceneInfo(sceneUUID));
    }

    /**
     * 删除词库
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
            return AjaxResult.success(dialogueSceneService.removeScene(ids));
    }
}
