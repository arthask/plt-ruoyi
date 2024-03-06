package com.ruoyi.system.service;

import com.ruoyi.system.domain.Word;

import java.util.List;

/**
 * 单词Service接口
 * 
 * @author ruoyi
 * @date 2023-12-28
 */
public interface IWordService 
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
     * 批量删除单词
     * 
     * @param ids 需要删除的单词主键集合
     * @return 结果
     */
    public int deleteWordByIds(Long[] ids);

    /**
     * 删除单词信息
     * 
     * @param id 单词主键
     * @return 结果
     */
    public int deleteWordById(Long id);

    /**
     * 导入单词
     * @param wordList 单词列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果信息
     */
    public String importWords(List<Word> wordList, Boolean isUpdateSupport, Long userId);

    /**
     * 获取一个单词详情
     * @return
     */
    Word getOneWord(Long userId, int index);

    List<Word> getNewWordOfUser(Long userId);
}
