package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LexiconWord;

/**
 * 词库与单词关系Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public interface LexiconWordMapper 
{
    /**
     * 查询词库与单词关系
     * 
     * @param id 词库与单词关系主键
     * @return 词库与单词关系
     */
    public LexiconWord selectLexiconWordById(Long id);

    /**
     * 查询词库与单词关系列表
     * 
     * @param lexiconWord 词库与单词关系
     * @return 词库与单词关系集合
     */
    public List<LexiconWord> selectLexiconWordList(LexiconWord lexiconWord);

    /**
     * 新增词库与单词关系
     * 
     * @param lexiconWord 词库与单词关系
     * @return 结果
     */
    public int insertLexiconWord(LexiconWord lexiconWord);

    /**
     * 修改词库与单词关系
     * 
     * @param lexiconWord 词库与单词关系
     * @return 结果
     */
    public int updateLexiconWord(LexiconWord lexiconWord);

    /**
     * 删除词库与单词关系
     * 
     * @param id 词库与单词关系主键
     * @return 结果
     */
    public int deleteLexiconWordById(Long id);

    /**
     * 批量删除词库与单词关系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLexiconWordByIds(Long[] ids);
}
