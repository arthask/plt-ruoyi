package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Question;
import com.ruoyi.system.domain.dto.QuestionDto;
import com.ruoyi.system.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 问题Controller
 * 
 * @author ruoyi
 * @date 2024-02-02
 */
@RestController
@RequestMapping("/system/question")
public class QuestionController extends BaseController
{
    @Autowired
    private IQuestionService questionService;

    /**
     * 查询问题列表
     */
    @PreAuthorize("@ss.hasPermi('system:question:list')")
    @GetMapping("/list")
    public TableDataInfo list(Question question)
    {
        startPage();
        List<Question> list = questionService.selectQuestionList(question);
        return getDataTable(list);
    }

    /**
     * 导出问题列表
     */
    @PreAuthorize("@ss.hasPermi('system:question:export')")
    @Log(title = "问题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Question question)
    {
        List<Question> list = questionService.selectQuestionList(question);
        ExcelUtil<Question> util = new ExcelUtil<Question>(Question.class);
        util.exportExcel(response, list, "问题数据");
    }

    /**
     * 获取问题详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:question:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(questionService.selectQuestionById(id));
    }

    /**
     * 新增问题
     */
    @PreAuthorize("@ss.hasPermi('system:question:add')")
    @Log(title = "问题", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated  @RequestBody QuestionDto questionData)
    {
        return AjaxResult.success(questionService.insertQuestion(questionData, this.getUserId()));
    }

    /**
     * 修改问题
     */
    @PreAuthorize("@ss.hasPermi('system:question:edit')")
    @Log(title = "问题", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Question question)
    {
        return toAjax(questionService.updateQuestion(question));
    }

    /**
     * 删除问题
     */
    @PreAuthorize("@ss.hasPermi('system:question:remove')")
    @Log(title = "问题", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(questionService.deleteQuestionByIds(ids));
    }
}
