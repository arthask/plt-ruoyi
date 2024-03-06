package com.ruoyi.web.controller.business;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserStudyRecord;
import com.ruoyi.system.service.IUserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 用户学习记录Controller
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@RestController
@RequestMapping("/system/record")
public class UserStudyRecordController extends BaseController {
    @Autowired
    private IUserStudyRecordService userStudyRecordService;

    /**
     * 查询用户学习记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserStudyRecord userStudyRecord) {
        startPage();
        List<UserStudyRecord> list = userStudyRecordService.selectUserStudyRecordList(userStudyRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户学习记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "用户学习记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserStudyRecord userStudyRecord) {
        List<UserStudyRecord> list = userStudyRecordService.selectUserStudyRecordList(userStudyRecord);
        ExcelUtil<UserStudyRecord> util = new ExcelUtil<UserStudyRecord>(UserStudyRecord.class);
        util.exportExcel(response, list, "用户学习记录数据");
    }

    /**
     * 获取用户学习记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(userStudyRecordService.selectUserStudyRecordById(id));
    }

    /**
     * 新增用户学习记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "用户学习记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserStudyRecord userStudyRecord) {
        userStudyRecord.setUserName(getUsername());
        userStudyRecord.setUserId(getUserId());
        userStudyRecord.setStudyTime(new Date());
        return toAjax(userStudyRecordService.insertUserStudyRecord(userStudyRecord));
    }

    /**
     * 修改用户学习记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "用户学习记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserStudyRecord userStudyRecord) {
        return toAjax(userStudyRecordService.updateUserStudyRecord(userStudyRecord));
    }

    /**
     * 删除用户学习记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "用户学习记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userStudyRecordService.deleteUserStudyRecordByIds(ids));
    }

    /**
     * 获取某个日期学习过的单词
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @Log(title = "获取某个日期学习过的单词")
    @GetMapping("/listWordOfDay/{day}")
    public TableDataInfo listWordOfDay(@PathVariable int day) {
        startPage();
        return getDataTable(userStudyRecordService.listWordOfDay(this.getUserId(), day));
    }
}
