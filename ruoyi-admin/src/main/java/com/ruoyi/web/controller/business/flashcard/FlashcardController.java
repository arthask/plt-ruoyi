package com.ruoyi.web.controller.business.flashcard;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.gencode.service.FlashcardService;
import com.ruoyi.system.domain.dto.flashcard.card.AddCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import static com.ruoyi.common.utils.SecurityUtils.getUserId;

/**
 * <p>
 * 闪卡表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@Controller
@RequestMapping("/gencode/flashcard")
public class FlashcardController {
    @Autowired
    private FlashcardService flashcardService;
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

    /**
     * 获取卡包中的一张闪卡
     */
    @PreAuthorize("@ss.hasPermi('system:flashcard:add')")
    @Log(title = "闪卡", businessType = BusinessType.OTHER)
    @GetMapping("/getCardOfPackage")
    public AjaxResult getCardOfPackage(@RequestParam("packageUUID") String packageUUID,
                                       @RequestParam("offset") Integer offset) {
        return AjaxResult.success(flashcardService.getCardOfPackage(packageUUID, offset));
    }
}
