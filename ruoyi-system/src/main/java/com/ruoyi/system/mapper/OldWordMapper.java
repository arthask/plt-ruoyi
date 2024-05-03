package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单词Mapper接口
 * 
 * @author ruoyi
 * @date 2023-12-28
 */
public interface OldWordMapper
{
    /**
     * 查询单词
     * 
     * @param id 单词主键
     * @return 单词
     */
    public Word selectWordById(Long id);

    /**
     * 查询单词列表
     * 
     * @param word 单词
     * @return 单词集合
     */
    public List<Word> selectWordList(Word word);

    /**
     * 新增单词
     * 
     * @param word 单词
     * @return 结果
     */
    public int insertWord(Word word);

    /**
     * 修改单词
     * 
     * @param word 单词
     * @return 结果
     */
    public int updateWord(Word word);

    /**
     * 删除单词
     * 
     * @param id 单词主键
     * @return 结果
     */
    public int deleteWordById(Long id);

    /**
     * 批量删除单词
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWordByIds(Long[] ids);

    public Word selectWordByName(@Param("word") String word, @Param("userId") Long userId);

    /**
     * 查询单词总数量
     * @param userId 用户id
     * @return 单词总数量
     */
    Long getWordCount(@Param("userId") Long userId);

    /**
     * 随机获取用户的一个单词
     * @param userId 用户id
     * @return
     */
    Word getRandomWordOfUser(@Param("userId") Long userId,@Param("lexiconId") Long lexiconId, @Param("offset") Long offset);

    /**
     * 随机获取用户的一个新单词
     * @param userId 用户id
     * @return 未学习的新单词
     */
    List<Word> getNewWordOfUser(@Param("userId") Long userId);

    /**
     * 查询用户未学习的新单词总数
     * @param userId 用户id
     * @return 未学习的新单词总数
     */
    Long getNewWordCountOfUser(@Param("userId") Long userId);


    Word getNewWordByIndex(@Param("userId") Long userId,
                              @Param("offset") int index);

    /**
     * 使用中文操作单词
     * @param searchCn 中文搜索内容
     * @return 匹配的单词
     */
    List<Word> searchWordByCN(@Param("searchCn") String searchCn);

    /**
     * 批量插入单词
     * @param wordList
     * @return
     */
    int insertBatch(List<Word> wordList);
}
