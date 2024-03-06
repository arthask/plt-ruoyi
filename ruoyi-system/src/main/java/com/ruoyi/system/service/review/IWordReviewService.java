package com.ruoyi.system.service.review;

import com.ruoyi.system.domain.Word;
import com.ruoyi.system.domain.vo.WordVo;

import java.util.List;

public interface IWordReviewService {
    /**
     * 获取复习单词
     * @param userId 用户id
     * @return 需复习单词
     */
    Word getReviewWord(Long userId);

    /**
     * 根据索引获取复习单词
     * @param userId 用户id
     * @param index 索引下标
     * @return 获取复习单词
     */
    WordVo getReviewWordByIndex(Long userId, int index);

    /**
     * 获取需要复习的单词集合
     * @param userId 用户id
     * @return 单词复习集
     */
    List<Word> getNeedReviewWords(Long userId);

}
