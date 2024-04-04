package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LexiconMapper;
import com.ruoyi.system.domain.Lexicon;
import com.ruoyi.system.service.ILexiconService;

/**
 * 词库Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@Service
public class LexiconServiceImpl implements ILexiconService 
{
    @Autowired
    private LexiconMapper lexiconMapper;

    /**
     * 查询词库
     * 
     * @param id 词库主键
     * @return 词库
     */
    @Override
    public Lexicon selectLexiconById(Long id)
    {
        return lexiconMapper.selectLexiconById(id);
    }

    /**
     * 查询词库列表
     * 
     * @param lexicon 词库
     * @return 词库
     */
    @Override
    public List<Lexicon> selectLexiconList(Lexicon lexicon)
    {
        return lexiconMapper.selectLexiconList(lexicon);
    }

    /**
     * 新增词库
     * 
     * @param lexicon 词库
     * @return 结果
     */
    @Override
    public int insertLexicon(Lexicon lexicon)
    {
        lexicon.setCreateTime(DateUtils.getNowDate());
        return lexiconMapper.insertLexicon(lexicon);
    }

    /**
     * 修改词库
     * 
     * @param lexicon 词库
     * @return 结果
     */
    @Override
    public int updateLexicon(Lexicon lexicon)
    {
        lexicon.setUpdateTime(DateUtils.getNowDate());
        return lexiconMapper.updateLexicon(lexicon);
    }

    /**
     * 批量删除词库
     * 
     * @param ids 需要删除的词库主键
     * @return 结果
     */
    @Override
    public int deleteLexiconByIds(Long[] ids)
    {
        return lexiconMapper.deleteLexiconByIds(ids);
    }

    /**
     * 删除词库信息
     * 
     * @param id 词库主键
     * @return 结果
     */
    @Override
    public int deleteLexiconById(Long id)
    {
        return lexiconMapper.deleteLexiconById(id);
    }
}
