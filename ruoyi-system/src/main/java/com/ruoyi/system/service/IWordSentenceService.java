package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.WordSentence;

/**
 * 单词例句Service接口
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public interface IWordSentenceService 
{
    /**
     * 查询单词例句
     * 
     * @param id 单词例句主键
     * @return 单词例句
     */
    public WordSentence selectWordSentenceById(Long id);

    /**
     * 查询单词例句列表
     * 
     * @param wordSentence 单词例句
     * @return 单词例句集合
     */
    public List<WordSentence> selectWordSentenceList(WordSentence wordSentence);

    /**
     * 新增单词例句
     * 
     * @param wordSentence 单词例句
     * @return 结果
     */
    public int insertWordSentence(WordSentence wordSentence);

    /**
     * 修改单词例句
     * 
     * @param wordSentence 单词例句
     * @return 结果
     */
    public int updateWordSentence(WordSentence wordSentence);

    /**
     * 批量删除单词例句
     * 
     * @param ids 需要删除的单词例句主键集合
     * @return 结果
     */
    public int deleteWordSentenceByIds(Long[] ids);

    /**
     * 删除单词例句信息
     * 
     * @param id 单词例句主键
     * @return 结果
     */
    public int deleteWordSentenceById(Long id);
}
