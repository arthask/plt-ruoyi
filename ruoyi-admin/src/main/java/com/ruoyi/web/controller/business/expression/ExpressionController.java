package com.ruoyi.web.controller.business.expression;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.dto.expression.ExpressionData;
import com.ruoyi.system.domain.dto.scene.SceneData;
import com.ruoyi.system.gencode.entity.Dialogue;
import com.ruoyi.system.gencode.entity.DialogueScene;
import com.ruoyi.system.gencode.entity.Expression;
import com.ruoyi.system.gencode.service.DialogueService;
import com.ruoyi.system.gencode.service.ExpressionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 表达记录表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@RestController
@RequestMapping("/expression")
public class ExpressionController extends BaseController {
    @Autowired
    private ExpressionService expressionService;

    /**
     * 查询对话场景列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "content", required = false) String name) {
        startPage();
        QueryWrapper<Expression> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("content", name);
        };
        queryWrapper.orderByDesc("create_time");
        List<Expression> list = expressionService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ExpressionData expressionData) {
        expressionData.setUserId(getUserId());
        return AjaxResult.success(expressionService.addExpressionData(expressionData));
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public AjaxResult update(@RequestBody ExpressionData expressionData) {
        expressionData.setUserId(getUserId());
        return AjaxResult.success(expressionService.updateExpressionData(expressionData));
    }

    /**
     * 获取详情
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(@RequestParam("expressionUUID") String expressionUUID) {
        return AjaxResult.success(expressionService.getInfo(expressionUUID));
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return AjaxResult.success(expressionService.removeExpressionData(ids));
    }
}
