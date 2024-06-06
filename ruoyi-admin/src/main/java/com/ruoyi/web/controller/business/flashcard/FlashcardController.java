package com.ruoyi.web.controller.business.flashcard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.gencode.entity.Flashcard;
import com.ruoyi.system.gencode.entity.FlashcardPackage;
import com.ruoyi.system.gencode.service.FlashcardService;
import com.ruoyi.system.domain.dto.flashcard.card.AddCardDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.ruoyi.common.utils.SecurityUtils.getUserId;

/**
 * <p>
 * 闪卡表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/flashcard")
public class FlashcardController extends BaseController {
    @Autowired
    private FlashcardService flashcardService;

    /**
     * 查询卡片列表
     */
    @GetMapping("/list")
    public TableDataInfo list() {
        startPage();
        QueryWrapper<Flashcard> queryWrapper = new QueryWrapper<>();
        List<Flashcard> list = flashcardService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 新增闪卡
     */
    @PreAuthorize("@ss.hasPermi('system:flashcard:add')")
    @Log(title = "闪卡", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody AddCardDto addCardDto) {
        Long userId = getUserId();
        return AjaxResult.success(flashcardService.addCard(addCardDto, userId));
    }

    @GetMapping("/getCardInfo/{uuid}")
    public AjaxResult getCardInfo(@PathVariable("uuid") String uuid) {
        Long userId = getUserId();
        return AjaxResult.success();
    }


    /**
     * 获取卡包中的一张闪卡
     */
    @GetMapping("/getCardOfPackage")
    public AjaxResult getCardOfPackage(@RequestParam("packageUUID") String packageUUID,
                                       @RequestParam("offset") Integer offset) {
        return AjaxResult.success(flashcardService.getCardOfPackage(packageUUID, offset));
    }
}
