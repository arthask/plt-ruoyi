package com.example.pltool.controller.business.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pltool.service.language.UserWordService;
import com.example.pltool.service.language.WordReviewService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;

@RestController
@RequestMapping("/review")
public class WordReviewController extends BaseController {

  @Autowired
  private WordReviewService wordReviewService;

  @Autowired
  private UserWordService newUserWordService;

  @GetMapping("/getReviewWord")
  public AjaxResult getReviewWord() {
    return AjaxResult.success(newUserWordService.getNeedReviewWord(this.getUserId()));
  }

  @GetMapping("/getReviewWordByIndex/{index}")
  public AjaxResult getReviewWordByIndex(@PathVariable Integer index) {
    return AjaxResult.success(wordReviewService.getReviewWordByIndex(this.getUserId(), index));
  }
}
