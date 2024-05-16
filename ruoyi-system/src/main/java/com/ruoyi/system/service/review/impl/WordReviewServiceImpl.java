package com.ruoyi.system.service.review.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.system.domain.Word;
import com.ruoyi.system.domain.vo.WordVo;
import com.ruoyi.system.mapper.OldUserWordMapper;
import com.ruoyi.system.mapper.OldWordMapper;
import com.ruoyi.system.service.review.IWordReviewService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("oldWordReviewService")
public class WordReviewServiceImpl implements IWordReviewService {
    @Autowired
    private OldUserWordMapper oldUserWordMapper;
    @Autowired
    private OldWordMapper oldWordMapper;

    @Override
    public Word getReviewWord(Long userId) {
        // 阶段+最新学习记录 =》 确定下次学习时间
        // 找到下次学习时间小于当前时间的用户单词，并按学习时间排序
        Long reviewWordId = oldUserWordMapper.getReviewWordId(DateUtil.now(), userId);
        if (Objects.isNull(reviewWordId)) {
            return null;
        }
        return oldWordMapper.selectWordById(reviewWordId);
    }

    @Override
    public WordVo getReviewWordByIndex(Long userId, int index) {
        // 阶段+最新学习记录 =》 确定下次学习时间
        // 找到下次学习时间小于当前时间的用户单词，并按学习时间排序
        Word word = oldUserWordMapper.getReviewWordByIndex(userId, index);
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
        return oldUserWordMapper.getNeedReviewWords(DateUtil.now(), userId);
    }
}
