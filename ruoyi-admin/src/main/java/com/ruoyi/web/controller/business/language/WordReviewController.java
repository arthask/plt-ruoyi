package com.ruoyi.web.controller.business.language;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.gencode.service.UserWordService;
import com.ruoyi.system.service.review.IWordReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class WordReviewController extends BaseController {

    @Autowired
    private IWordReviewService wordReviewService;

    @Autowired
    private UserWordService newUserWordService;

    @GetMapping("/getReviewWord")
    public AjaxResult getReviewWord() {
        return AjaxResult.success(newUserWordService.getNeedReviewWord(this.getUserId()));
    }

    @GetMapping("/getReviewWordByIndex/{index}")
    public AjaxResult getReviewWordByIndex(@PathVariable Integer index) {
        return AjaxResult.success(wordReviewService.getReviewWordByIndex(this.getUserId(),index));
    }
}
