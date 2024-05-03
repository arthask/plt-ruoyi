package com.ruoyi.web.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.QuestionDto;
import com.ruoyi.system.gencode.entity.Question;
import com.ruoyi.system.gencode.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 问题Controller
 *
 * @author ruoyi
 * @date 2024-02-02
 */
@RestController
@RequestMapping("/system/question")
public class QuestionController extends BaseController {

    @Autowired
    private QuestionService newQuestionService;

    /**
     * 查询问题列表
     */
    @PreAuthorize("@ss.hasPermi('system:question:list')")
    @GetMapping("/list")
    public TableDataInfo list(Question question) {
        startPage();
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Question> list = newQuestionService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取问题详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:question:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(newQuestionService.getById(id));
    }

    /**
     * 新增问题
     */
    @PreAuthorize("@ss.hasPermi('system:question:add')")
    @Log(title = "问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody QuestionDto questionData) {
        return AjaxResult.success(newQuestionService.insertQuestion(questionData, this.getUserId()));
    }

    /**
     * 修改问题
     */
    @PreAuthorize("@ss.hasPermi('system:question:edit')")
    @Log(title = "问题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Question question) {
        return toAjax(newQuestionService.updateById(question));
    }

    /**
     * 删除问题
     */
    @PreAuthorize("@ss.hasPermi('system:question:remove')")
    @Log(title = "问题", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(newQuestionService.removeBatchByIds(Arrays.asList(ids)));
    }
}
