package com.ruoyi.web.controller.business.flashcard;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.dto.flashcard.cardattribute.StudyCardDto;
import com.ruoyi.system.gencode.service.FlashcardAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 闪卡表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/flashcardAttribute")
public class FlashcardAttributeController {

    @Autowired
    private FlashcardAttributeService flashcardAttributeService;

    @PostMapping("/studyCard")
    AjaxResult studyCard(@RequestBody StudyCardDto studyCardDto) {
        flashcardAttributeService.studyCard(studyCardDto.getCardUUID(), studyCardDto.getFamiliarity());
        return AjaxResult.success();
    }

    @GetMapping("/getClassifyCount")
    AjaxResult getClassifyCount(@RequestParam("packageUUID") String packageUUID) {
        return AjaxResult.success(flashcardAttributeService.getClassifyCount(packageUUID));
    }
}
