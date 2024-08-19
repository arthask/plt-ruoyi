package com.example.pltool.controller.business.language;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pltool.domain.entity.UserStudyRecord;
import com.example.pltool.service.language.UserStudyRecordService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    private UserStudyRecordService newUserStudyRecordService;

    /**
     * 查询用户学习记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserStudyRecord userStudyRecord) {
        startPage();
        QueryWrapper<UserStudyRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("study_time");
        List<UserStudyRecord> result = newUserStudyRecordService.list(queryWrapper);
        return getDataTable(result);
    }


    /**
     * 获取用户学习记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(newUserStudyRecordService.getById(id));
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
        return toAjax(newUserStudyRecordService.insertUserStudyRecord(userStudyRecord));
    }

    /**
     * 修改用户学习记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "用户学习记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserStudyRecord userStudyRecord) {
       return AjaxResult.success();
    }

    /**
     * 删除用户学习记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "用户学习记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return AjaxResult.success();
    }

    /**
     * 获取某个日期学习过的单词
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @Log(title = "获取某个日期学习过的单词")
    @GetMapping("/listWordOfDay/{day}")
    public TableDataInfo listWordOfDay(@PathVariable int day) {
        startPage();
        return getDataTable(newUserStudyRecordService.listWordOfDay(this.getUserId(), day));
    }

    /**
     * 获取某个单词的学习记录
     *
     * @param wordUUId
     * @return
     */
    @GetMapping("/getStudyRecordsOfWord")
    public TableDataInfo getStudyRecordsOfWord(@RequestParam("wordUUId") String wordUUId) {
        startPage();
        return getDataTable(newUserStudyRecordService.getStudyRecordsOfWord(wordUUId));
    }
}
