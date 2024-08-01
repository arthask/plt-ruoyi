package com.example.pltool.service.impl.language;

import cn.hutool.core.date.DateUtil;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.mapper.UserWordMapper;
import com.example.pltool.mapper.WordMapper;
import com.example.pltool.service.language.WordReviewService;
import com.example.pltool.domain.vo.WordVo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;
@Service
public class WordReviewServiceImpl implements WordReviewService {
    @Autowired
    private UserWordMapper userWordMapper;
    @Autowired
    private WordMapper wordMapper;

    @Override
    public Word getReviewWord(Long userId) {
        // 阶段+最新学习记录 =》 确定下次学习时间
        // 找到下次学习时间小于当前时间的用户单词，并按学习时间排序
        Long reviewWordId = userWordMapper.getReviewWordId(DateUtil.now(), userId);
        if (Objects.isNull(reviewWordId)) {
            return null;
        }
        return wordMapper.selectById(reviewWordId);
    }

    @Override
    public WordVo getReviewWordByIndex(Long userId, int index) {
        // 阶段+最新学习记录 =》 确定下次学习时间
        // 找到下次学习时间小于当前时间的用户单词，并按学习时间排序
        Word word = userWordMapper.getReviewWordByIndex(userId, index);
        if (Objects.isNull(word)) {
            return null;
        }
        WordVo wordVo = new WordVo();
        BeanUtils.copyProperties(word, wordVo);
        wordVo.setIndex(index);
        return wordVo;
    }

    @Override
    public List<Word> getNeedReviewWords(Long userId) {
        return userWordMapper.getNeedReviewWords(DateUtil.now(), userId);
    }
}