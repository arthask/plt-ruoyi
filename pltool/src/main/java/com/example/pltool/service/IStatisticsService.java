package com.example.pltool.service;


import com.example.pltool.domain.vo.StatisticsCountVo;
import com.example.pltool.domain.vo.UserWordPeriodVo;

import java.util.List;
import java.util.Map;

public interface IStatisticsService {
    /**
     * 获取数量统计信息
     * @return 数量统计信息
     */
    StatisticsCountVo getStatisticsOfCount(Long userId);

    /**
     * 查询每一个月的学习记录数量
     * @return
     */
    Long [] getStudyRecordOfMonth(Long userId);

    /**
     * 查询每一个月的学习单词数量
     * @return
     */
    Long [] getUserWordOfMonth(Long userId);

    /**
     * 查询单词的阶段数量
     * @return 阶段数量集合
     */
    List<UserWordPeriodVo> getUserWordPeriodCount(Long userId);

    /**
     * 查询一个月中每天的期望值与实际值
     */
    Map<String, Long[]> getExceptAndActualValueOfDay(Long userId);

    /**
     * 查询用户总单词数，及未学习的新单词数
     * @param userId 用户id
     * @return 用户总单词数，及未学习的新单词数
     */
    Map<String, Long> getTotalAndNotStudyNum(Long userId);

    /**
     * 查询用户今日需复习单词总数、已复习单词数
     * @param userId 用户id
     * @return 用户今日需复习单词总数、已复习单词数
     */
    Map<String ,Long> getNeedReviewAnHaveReviewNum(Long userId);
}
