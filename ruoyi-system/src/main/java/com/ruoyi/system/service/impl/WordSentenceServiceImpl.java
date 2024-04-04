package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.WordSentenceMapper;
import com.ruoyi.system.domain.WordSentence;
import com.ruoyi.system.service.IWordSentenceService;

/**
 * 单词例句Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@Service
public class WordSentenceServiceImpl implements IWordSentenceService 
{
    @Autowired
    private WordSentenceMapper wordSentenceMapper;

    /**
     * 查询单词例句
     * 
     * @param id 单词例句主键
     * @return 单词例句
     */
    @Override
    public WordSentence selectWordSentenceById(Long id)
    {
        return wordSentenceMapper.selectWordSentenceById(id);
    }

    /**
     * 查询单词例句列表
     * 
     * @param wordSentence 单词例句
     * @return 单词例句
     */
    @Override
    public List<WordSentence> selectWordSentenceList(WordSentence wordSentence)
    {
        return wordSentenceMapper.selectWordSentenceList(wordSentence);
    }

    /**
     * 新增单词例句
     * 
     * @param wordSentence 单词例句
     * @return 结果
     */
    @Override
    public int insertWordSentence(WordSentence wordSentence)
    {
        wordSentence.setCreateTime(DateUtils.getNowDate());
        return wordSentenceMapper.insertWordSentence(wordSentence);
    }

    /**
     * 修改单词例句
     * 
     * @param wordSentence 单词例句
     * @return 结果
     */
    @Override
    public int updateWordSentence(WordSentence wordSentence)
    {
        wordSentence.setUpdateTime(DateUtils.getNowDate());
        return wordSentenceMapper.updateWordSentence(wordSentence);
    }

    /**
     * 批量删除单词例句
     * 
     * @param ids 需要删除的单词例句主键
     * @return 结果
     */
    @Override
    public int deleteWordSentenceByIds(Long[] ids)
    {
        return wordSentenceMapper.deleteWordSentenceByIds(ids);
    }

    /**
     * 删除单词例句信息
     * 
     * @param id 单词例句主键
     * @return 结果
     */
    @Override
    public int deleteWordSentenceById(Long id)
    {
        return wordSentenceMapper.deleteWordSentenceById(id);
    }
}
