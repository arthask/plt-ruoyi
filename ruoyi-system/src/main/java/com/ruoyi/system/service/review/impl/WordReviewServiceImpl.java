package com.ruoyi.system.service.review.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.system.domain.Word;
import com.ruoyi.system.domain.vo.WordVo;
import com.ruoyi.system.mapper.UserWordMapper;
import com.ruoyi.system.mapper.WordMapper;
import com.ruoyi.system.service.review.IWordReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class WordReviewServiceImpl implements IWordReviewService {
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
        return wordMapper.selectWordById(reviewWordId);
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
