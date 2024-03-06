package com.ruoyi.web.controller.conversation;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Dialogue;
import com.ruoyi.system.service.IDialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 对话Controller
 * 
 * @author ruoyi
 * @date 2024-02-21
 */
@RestController
@RequestMapping("/system/dialogue")
public class DialogueController extends BaseController
{
    @Autowired
    private IDialogueService dialogueService;

    /**
     * 查询对话列表
     */
    @PreAuthorize("@ss.hasPermi('system:dialogue:list')")
    @GetMapping("/list")
    public TableDataInfo list(Dialogue dialogue)
    {
        startPage();
        List<Dialogue> list = dialogueService.selectDialogueList(dialogue);
        return getDataTable(list);
    }

    /**
     * 导出对话列表
     */
    @PreAuthorize("@ss.hasPermi('system:dialogue:export')")
    @Log(title = "对话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Dialogue dialogue)
    {
        List<Dialogue> list = dialogueService.selectDialogueList(dialogue);
        ExcelUtil<Dialogue> util = new ExcelUtil<Dialogue>(Dialogue.class);
        util.exportExcel(response, list, "对话数据");
    }

    /**
     * 获取对话详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:dialogue:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(dialogueService.selectDialogueById(id));
    }

    /**
     * 新增对话
     */
    @PreAuthorize("@ss.hasPermi('system:dialogue:add')")
    @Log(title = "对话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Dialogue dialogue)
    {
        return toAjax(dialogueService.insertDialogue(dialogue));
    }

    /**
     * 修改对话
     */
    @PreAuthorize("@ss.hasPermi('system:dialogue:edit')")
    @Log(title = "对话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Dialogue dialogue)
    {
        return toAjax(dialogueService.updateDialogue(dialogue));
    }

    /**
     * 删除对话
     */
    @PreAuthorize("@ss.hasPermi('system:dialogue:remove')")
    @Log(title = "对话", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(dialogueService.deleteDialogueByIds(ids));
    }
}
