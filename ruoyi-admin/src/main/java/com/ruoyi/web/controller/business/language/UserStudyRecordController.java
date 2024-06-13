package com.ruoyi.web.controller.business.language;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.UserStudyRecord;
import com.ruoyi.system.gencode.service.UserStudyRecordService;
import com.ruoyi.system.gencode.service.UserWordService;
import com.ruoyi.system.service.IUserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    @Resource(name = "oldUserStudyRecordService")
    private IUserStudyRecordService userStudyRecordService;

    @Autowired
    private UserStudyRecordService newUserStudyRecordService;

    /**
     * 查询用户学习记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserStudyRecord userStudyRecord) {
        startPage();
        QueryWrapper<com.ruoyi.system.gencode.entity.UserStudyRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("study_time");
        List<com.ruoyi.system.gencode.entity.UserStudyRecord> result = newUserStudyRecordService.list(queryWrapper);
        return getDataTable(result);
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
    public AjaxResult add(@RequestBody com.ruoyi.system.gencode.entity.UserStudyRecord userStudyRecord) {
        userStudyRecord.setUserName(getUsername());
        userStudyRecord.setUserId(getUserId());
        return toAjax(newUserStudyRecordService.insertUserStudyRecord(userStudyRecord));
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
