package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Lexicon;

/**
 * 词库Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public interface LexiconMapper 
{
    /**
     * 查询词库
     * 
     * @param id 词库主键
     * @return 词库
     */
    public Lexicon selectLexiconById(Long id);

    /**
     * 查询词库列表
     * 
     * @param lexicon 词库
     * @return 词库集合
     */
    public List<Lexicon> selectLexiconList(Lexicon lexicon);

    /**
     * 新增词库
     * 
     * @param lexicon 词库
     * @return 结果
     */
    public int insertLexicon(Lexicon lexicon);

    /**
     * 修改词库
     * 
     * @param lexicon 词库
     * @return 结果
     */
    public int updateLexicon(Lexicon lexicon);

    /**
     * 删除词库
     * 
     * @param id 词库主键
     * @return 结果
     */
    public int deleteLexiconById(Long id);

    /**
     * 批量删除词库
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLexiconByIds(Long[] ids);
}
