package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LexiconWordMapper;
import com.ruoyi.system.domain.LexiconWord;
import com.ruoyi.system.service.ILexiconWordService;

/**
 * 词库与单词关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@Service
public class LexiconWordServiceImpl implements ILexiconWordService 
{
    @Autowired
    private LexiconWordMapper lexiconWordMapper;

    /**
     * 查询词库与单词关系
     * 
     * @param id 词库与单词关系主键
     * @return 词库与单词关系
     */
    @Override
    public LexiconWord selectLexiconWordById(Long id)
    {
        return lexiconWordMapper.selectLexiconWordById(id);
    }

    /**
     * 查询词库与单词关系列表
     * 
     * @param lexiconWord 词库与单词关系
     * @return 词库与单词关系
     */
    @Override
    public List<LexiconWord> selectLexiconWordList(LexiconWord lexiconWord)
    {
        return lexiconWordMapper.selectLexiconWordList(lexiconWord);
    }

    /**
     * 新增词库与单词关系
     * 
     * @param lexiconWord 词库与单词关系
     * @return 结果
     */
    @Override
    public int insertLexiconWord(LexiconWord lexiconWord)
    {
        lexiconWord.setCreateTime(DateUtils.getNowDate());
        return lexiconWordMapper.insertLexiconWord(lexiconWord);
    }

    /**
     * 修改词库与单词关系
     * 
     * @param lexiconWord 词库与单词关系
     * @return 结果
     */
    @Override
    public int updateLexiconWord(LexiconWord lexiconWord)
    {
        lexiconWord.setUpdateTime(DateUtils.getNowDate());
        return lexiconWordMapper.updateLexiconWord(lexiconWord);
    }

    /**
     * 批量删除词库与单词关系
     * 
     * @param ids 需要删除的词库与单词关系主键
     * @return 结果
     */
    @Override
    public int deleteLexiconWordByIds(Long[] ids)
    {
        return lexiconWordMapper.deleteLexiconWordByIds(ids);
    }

    /**
     * 删除词库与单词关系信息
     * 
     * @param id 词库与单词关系主键
     * @return 结果
     */
    @Override
    public int deleteLexiconWordById(Long id)
    {
        return lexiconWordMapper.deleteLexiconWordById(id);
    }
}
