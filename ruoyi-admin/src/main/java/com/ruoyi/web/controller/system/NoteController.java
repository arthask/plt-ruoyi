package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Note;
import com.ruoyi.system.domain.vo.NoteInfoVo;
import com.ruoyi.system.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 笔记Controller
 * 
 * @author ruoyi
 * @date 2024-02-02
 */
@RestController
@RequestMapping("/system/note")
public class NoteController extends BaseController
{
    @Autowired
    private INoteService noteService;

    /**
     * 查询笔记列表
     */
    @PreAuthorize("@ss.hasPermi('system:note:list')")
    @GetMapping("/list")
    public TableDataInfo list(Note note)
    {
        startPage();
        List<Note> list = noteService.selectNoteList(note);
        return getDataTable(list);
    }

    /**
     * 导出笔记列表
     */
    @PreAuthorize("@ss.hasPermi('system:note:export')")
    @Log(title = "笔记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Note note)
    {
        List<Note> list = noteService.selectNoteList(note);
        ExcelUtil<Note> util = new ExcelUtil<Note>(Note.class);
        util.exportExcel(response, list, "笔记数据");
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(noteService.selectNoteById(id));
    }

    /**
     * 新增笔记
     */
    @PreAuthorize("@ss.hasPermi('system:note:add')")
    @Log(title = "笔记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Note note)
    {
        return toAjax(noteService.insertNote(note));
    }

    /**
     * 修改笔记
     */
    @PreAuthorize("@ss.hasPermi('system:note:edit')")
    @Log(title = "笔记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Note note)
    {
        return toAjax(noteService.updateNote(note));
    }

    /**
     * 删除笔记
     */
    @PreAuthorize("@ss.hasPermi('system:note:remove')")
    @Log(title = "笔记", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(noteService.deleteNoteByIds(ids));
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @GetMapping(value = "/getNoteInfo/{id}")
    public AjaxResult getNoteInfo(@PathVariable("id") Long id) {
        return success(noteService.getNoteInfo(id));
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @PostMapping(value = "/updateNoteInfo")
    public AjaxResult updateNoteInfo(@RequestBody NoteInfoVo noteInfoVo) {
        return success(noteService.updateNoteInfo(noteInfoVo));
    }
}
