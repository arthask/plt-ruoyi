package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.language.wordsentence.WordSentenceDto;
import com.example.pltool.domain.entity.WordSentence;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

/**
 * <p>
 * 单词例句表 服务类
 * </p>
 *
 * @author author
 * @since 2024-08-28
 */
public interface IWordSentenceService extends IService<WordSentence> {
    List<WordSentence> getSentencesByWordUUId(String wordUUId);

    AjaxResult addSentenceOfWord(WordSentenceDto wordSentenceDto);

    AjaxResult editSentenceOfWord(WordSentenceDto wordSentenceDto);
}
