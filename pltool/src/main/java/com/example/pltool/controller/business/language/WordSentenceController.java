package com.example.pltool.controller.business.language;

import static com.ruoyi.common.core.domain.AjaxResult.success;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.pltool.domain.dto.language.wordsentence.WordSentenceDto;
import com.example.pltool.service.IWordSentenceService;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * <p>
 * 单词例句表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-08-28
 */
@RestController
@RequestMapping("/pltool/wordSentence")
public class WordSentenceController {
  @Autowired
  private IWordSentenceService wordSentenceService;

  @GetMapping("/getSentence")
  public AjaxResult getSentencesByWordUUId(@RequestParam("wordUUId") String wordUUId) {
    return success(wordSentenceService.getSentencesByWordUUId(wordUUId));
  }

  @PostMapping("/addSentenceOfWord")
  public AjaxResult addSentenceOfWord(@RequestBody WordSentenceDto wordSentenceDto) {
    return AjaxResult.success(wordSentenceService.addSentenceOfWord(wordSentenceDto));
  }

  @PostMapping("/editSentenceOfWord")
  public AjaxResult editSentenceOfWord(@RequestBody WordSentenceDto wordSentenceDto) {
    return AjaxResult.success(wordSentenceService.editSentenceOfWord(wordSentenceDto));
  }
}
