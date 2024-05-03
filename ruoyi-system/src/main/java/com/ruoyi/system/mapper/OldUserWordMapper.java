package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserWord;
import com.ruoyi.system.domain.Word;
import com.ruoyi.system.domain.vo.UserWordPeriodVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户单词Mapper接口
 * 
 * @author ruoyi
 * @date 2024-01-06
 */
public interface OldUserWordMapper
{
    /**
     * 查询用户单词
     * 
     * @param id 用户单词主键
     * @return 用户单词
     */
    public UserWord selectUserWordById(Long id);

    /**
     * 查询用户单词列表
     * 
     * @param userWord 用户单词
     * @return 用户单词集合
     */
    public List<UserWord> selectUserWordList(UserWord userWord);

    /**
     * 新增用户单词
     * 
     * @param userWord 用户单词
     * @return 结果
     */
    public int insertUserWord(UserWord userWord);

    /**
     * 修改用户单词
     * 
     * @param userWord 用户单词
     * @return 结果
     */
    public int updateUserWord(UserWord userWord);

    /**
     * 删除用户单词
     * 
     * @param id 用户单词主键
     * @return 结果
     */
    public int deleteUserWordById(Long id);

    /**
     * 批量删除用户单词
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserWordByIds(Long[] ids);

    /**
     * 获取用户单词数量
     * @return 单词数量
     */
    Long getWordCount(@Param("userId") Long userId);

    /**
     * 使用单词查找
     * @param word
     * @return
     */
    UserWord findByWordWord(String word);

    /**
     * 获取单词的阶段数量
     * @return 单词阶段数量
     */
    List<UserWordPeriodVo> getUserWordPeriodCount(@Param("userId") Long userId);

    /**
     * 获取复习单词的id
     * @param timeStr 查询时间
     * @return 复习单词的id
     */
    Long getReviewWordId(@Param("timeStr") String timeStr, @Param("userId") Long userId);

    /**
     * 每月学习单词数统计
     * @param beginStr 年开始时间
     * @param endStr 年结束时间
     * @return 每月学习单词数统计
     */
    List<Long> getUserWordOfMonth(@Param("userId") Long userId,
                                  @Param("beginStr") String beginStr,
                                  @Param("endStr")String endStr);

    /**
     * 每天期望值
     * @param beginStr 月开始时间
     * @param endStr 月结束时间
     * @return 每天期望值
     */
    @MapKey("day")
    Map<String,Map<String, Long>> getExceptValueOfDay(@Param("userId") Long userId,
                                                      @Param("beginStr") String beginStr,
                                                      @Param("endStr")String endStr);

    /**
     * 查询用户今日需复习单词总数
     * @param userId 用户id
     * @param beginStr 日开始时间
     * @param endStr 日结束时间
     * @return 用户今日需复习单词总数
     */
    Long getNeedReviewNumOfDay(@Param("userId") Long userId,
                              @Param("beginStr") String beginStr,
                              @Param("endStr")String endStr);

    /**
     * 查询用户今日已复习单词数
     * @param userId 用户id
     * @param beginStr 日开始时间
     * @param endStr 日结束时间
     * @return 用户今日已复习单词数
     */
    Long getHaveReviewNumOfDay(@Param("userId") Long userId,
                               @Param("beginStr") String beginStr,
                               @Param("endStr")String endStr);

    Word getReviewWordByIndex(@Param("userId") Long userId,
                              @Param("offset") int index);

    Long getTotalReviewNum(@Param("userId") Long userId);

    List<Word> getNeedReviewWords(@Param("timeStr") String timeStr,
                                 @Param("userId") Long userId);
}
