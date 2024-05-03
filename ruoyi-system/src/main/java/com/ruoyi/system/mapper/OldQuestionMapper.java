package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 问题Mapper接口
 * 
 * @author ruoyi
 * @date 2024-02-02
 */
public interface OldQuestionMapper
{
    /**
     * 查询问题
     * 
     * @param id 问题主键
     * @return 问题
     */
    public Question selectQuestionById(Long id);

    /**
     * 查询问题列表
     * 
     * @param question 问题
     * @return 问题集合
     */
    public List<Question> selectQuestionList(Question question);

    /**
     * 新增问题
     * 
     * @param question 问题
     * @return 结果
     */
    public int insertQuestion(Question question);

    /**
     * 修改问题
     * 
     * @param question 问题
     * @return 结果
     */
    public int updateQuestion(Question question);

    /**
     * 删除问题
     * 
     * @param id 问题主键
     * @return 结果
     */
    public int deleteQuestionById(Long id);

    /**
     * 批量删除问题
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQuestionByIds(Long[] ids);

    /**
     * 批量新增问题
     *
     * @param questions 问题
     * @return 结果
     */
    int insertQuestionBatch(List<Question> questions);

    List<Question> selectByNoteId(Long noteId);

    int updateQuestionBatch(@Param("list") List<Question> questions);
}
