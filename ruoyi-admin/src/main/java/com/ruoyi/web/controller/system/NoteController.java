package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.vo.NoteInfoVo;
import com.ruoyi.system.gencode.entity.Note;
import com.ruoyi.system.gencode.service.NoteService;
import com.ruoyi.system.service.INoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 笔记Controller
 *
 * @author ruoyi
 * @date 2024-02-02
 */
@RestController
@RequestMapping("/system/note")
public class NoteController extends BaseController {

    @Autowired
    private NoteService newNoteService;

    /**
     * 查询笔记列表
     */
    @PreAuthorize("@ss.hasPermi('system:note:list')")
    @GetMapping("/list")
    public TableDataInfo list(Note note) {
        startPage();
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Note> list = newNoteService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(newNoteService.getById(id));
    }

    /**
     * 新增笔记
     */
    @PreAuthorize("@ss.hasPermi('system:note:add')")
    @Log(title = "笔记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Note note) {
        return toAjax(newNoteService.save(note));
    }

    /**
     * 修改笔记
     */
    @PreAuthorize("@ss.hasPermi('system:note:edit')")
    @Log(title = "笔记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Note note) {
        QueryWrapper<Note> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("uuid", note.getUuid());
        return toAjax(newNoteService.update(note, updateWrapper));
    }

    /**
     * 删除笔记
     */
    @PreAuthorize("@ss.hasPermi('system:note:remove')")
    @Log(title = "笔记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(newNoteService.removeBatchByIds(Arrays.asList(ids)));
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @GetMapping(value = "/getNoteInfo/{id}")
    public AjaxResult getNoteInfo(@PathVariable("id") Long id) {
        return success(newNoteService.getNoteInfo(id));
    }

    /**
     * 获取笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:note:query')")
    @PostMapping(value = "/updateNoteInfo")
    public AjaxResult updateNoteInfo(@RequestBody NoteInfoVo noteInfoVo) {
        return success(newNoteService.updateNoteInfo(noteInfoVo));
    }
}
