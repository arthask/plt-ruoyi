package com.example.pltool.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.language.wordsentence.SentenceDto;
import com.example.pltool.domain.dto.language.wordsentence.WordSentenceDto;
import com.example.pltool.domain.entity.WordSentence;
import com.example.pltool.mapper.WordSentenceMapper;
import com.example.pltool.service.IWordSentenceService;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * <p>
 * 单词例句表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-08-28
 */
@Service
public class WordSentenceServiceImpl extends ServiceImpl<WordSentenceMapper, WordSentence>
    implements IWordSentenceService {
  @Override
  public List<WordSentence> getSentencesByWordUUId(String wordUUId) {
    QueryWrapper<WordSentence> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("word_uuid", wordUUId);
    return list(queryWrapper);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult addSentenceOfWord(WordSentenceDto wordSentenceDto) {
    List<WordSentence> wordSentenceList = new ArrayList<>();
    wordSentenceDto.getSentenceDtoList().forEach(e -> {
      WordSentence wordSentence = new WordSentence();
      wordSentence.setSentenceContent(e.getSentenceContent())
          .setTranslateContent(e.getTranslateContent()).setWordUuid(wordSentenceDto.getWordUUId())
          .setUuid(UUID.randomUUID().toString().replace("-", ""));
      wordSentenceList.add(wordSentence);
    });
    if (!CollectionUtils.isEmpty(wordSentenceList)) {
      saveBatch(wordSentenceList);
    }
    return AjaxResult.success(true);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult editSentenceOfWord(WordSentenceDto wordSentenceDto) {
    // 查询库中已有的
    List<WordSentence> sentenceList = getSentencesByWordUUId(wordSentenceDto.getWordUUId());
    if (CollectionUtils.isEmpty(sentenceList)) {
      // 全部为新增
      return addSentenceOfWord(wordSentenceDto);
    }
    // 处理新增
    WordSentenceDto addSentenceDto = new WordSentenceDto();
    addSentenceDto.setWordUUId(wordSentenceDto.getWordUUId());
    List<SentenceDto> addSentenceDtoList = wordSentenceDto.getSentenceDtoList().stream()
        .filter(e -> StringUtils.isBlank(e.getUuid())).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(addSentenceDtoList)) {
      addSentenceDto.setSentenceDtoList(addSentenceDtoList);
      addSentenceOfWord(addSentenceDto);
    }
    Map<String, WordSentence> sentenceMap =
        sentenceList.stream().collect(Collectors.toMap(WordSentence::getUuid, Function.identity()));
    // 对修改的进行处理
    List<WordSentence> updateupdateSentenceList = new ArrayList<>();
    wordSentenceDto.getSentenceDtoList().stream().filter(e -> StringUtils.isNotBlank(e.getUuid()))
        .forEach(e -> {
          // 需要更新的数据
          WordSentence wordSentence = sentenceMap.get(e.getUuid());
          if (Objects.isNull(wordSentence)) {
            return;
          }
          WordSentence updateSentence = new WordSentence();
          updateSentence.setId(wordSentence.getId());
          boolean isAdd = false;
          if (!Objects.equals(wordSentence.getSentenceContent(), e.getSentenceContent())) {
            updateSentence.setSentenceContent(e.getSentenceContent());
            isAdd = true;
          }
          if (!Objects.equals(wordSentence.getTranslateContent(), e.getTranslateContent())) {
            updateSentence.setTranslateContent(e.getTranslateContent());
            isAdd = true;
          }
          if (isAdd) {
            updateupdateSentenceList.add(updateSentence);
          }
        });
    if (!CollectionUtils.isEmpty(updateupdateSentenceList)) {
      updateBatchById(updateupdateSentenceList);
    }
    // 对删除的进行处理
    if (!CollectionUtils.isEmpty(wordSentenceDto.getRemoveSentenceUUIdList())) {
      QueryWrapper<WordSentence> queryWrapper = new QueryWrapper<>();
      queryWrapper.in("uuid", wordSentenceDto.getRemoveSentenceUUIdList());
      remove(queryWrapper);
    }
    return AjaxResult.success(true);
  }
}
