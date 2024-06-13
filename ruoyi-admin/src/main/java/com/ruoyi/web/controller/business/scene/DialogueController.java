package com.ruoyi.web.controller.business.scene;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.gencode.entity.Dialogue;
import com.ruoyi.system.gencode.entity.DialogueScene;
import com.ruoyi.system.gencode.service.DialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 对话表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@RestController
@RequestMapping("/dialogue")
public class DialogueController extends BaseController {

    @Autowired
    private DialogueService dialogueService;

    /**
     * 查询对话列表
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        QueryWrapper<Dialogue> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<Dialogue> list = dialogueService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取对话详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(dialogueService.getById(id));
    }

    /**
     * 新增对话
     */
    @PostMapping
    public AjaxResult add(@RequestBody Dialogue dialogue) {
        return toAjax(dialogueService.save(dialogue));
    }
}
