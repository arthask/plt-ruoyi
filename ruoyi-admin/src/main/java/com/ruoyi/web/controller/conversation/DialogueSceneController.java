package com.ruoyi.web.controller.conversation;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.DialogueScene;
import com.ruoyi.system.domain.dto.GetReplyRequest;
import com.ruoyi.system.domain.dto.SceneData;
import com.ruoyi.system.service.IDialogueSceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 对话场景Controller
 *
 * @author ruoyi
 * @date 2024-02-21
 */
@RestController
@RequestMapping("/system/scene")
public class DialogueSceneController extends BaseController {
    @Autowired
    private IDialogueSceneService dialogueSceneService;

    /**
     * 查询对话场景列表
     */
    @PreAuthorize("@ss.hasPermi('system:scene:list')")
    @GetMapping("/list")
    public TableDataInfo list(DialogueScene dialogueScene) {
        startPage();
        List<DialogueScene> list = dialogueSceneService.selectDialogueSceneList(dialogueScene);
        return getDataTable(list);
    }

    /**
     * 导出对话场景列表
     */
    @PreAuthorize("@ss.hasPermi('system:scene:export')")
    @Log(title = "对话场景", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DialogueScene dialogueScene) {
        List<DialogueScene> list = dialogueSceneService.selectDialogueSceneList(dialogueScene);
        ExcelUtil<DialogueScene> util = new ExcelUtil<DialogueScene>(DialogueScene.class);
        util.exportExcel(response, list, "对话场景数据");
    }

    /**
     * 获取对话场景详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:scene:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dialogueSceneService.selectDialogueSceneById(id));
    }

    /**
     * 新增对话场景
     */
    @PreAuthorize("@ss.hasPermi('system:scene:add')")
    @Log(title = "对话场景", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DialogueScene dialogueScene) {
        return toAjax(dialogueSceneService.insertDialogueScene(dialogueScene));
    }

    /**
     * 修改对话场景
     */
    @PreAuthorize("@ss.hasPermi('system:scene:edit')")
    @Log(title = "对话场景", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DialogueScene dialogueScene) {
        return toAjax(dialogueSceneService.updateDialogueScene(dialogueScene));
    }

    /**
     * 删除对话场景
     */
    @PreAuthorize("@ss.hasPermi('system:scene:remove')")
    @Log(title = "对话场景", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(dialogueSceneService.deleteDialogueSceneByIds(ids));
    }


    /**
     * 新增对话场景
     */
    @PreAuthorize("@ss.hasPermi('system:scene:add')")
    @Log(title = "新增对话场景", businessType = BusinessType.INSERT)
    @PostMapping("/addDialogueScene")
    public AjaxResult addDialogueScene(@RequestBody SceneData sceneData) {
        sceneData.setUserId(getUserId());
        return AjaxResult.success(dialogueSceneService.addDialogueScene(sceneData));
    }

    @GetMapping(value = "/getReplayInfo")
    public AjaxResult getReplayInfo(GetReplyRequest getReplyRequest) {
        return AjaxResult.success(dialogueSceneService.getReplayInfo(getReplyRequest));
    }
}
