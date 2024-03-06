package com.ruoyi.system.service;

import com.ruoyi.system.domain.Question;
import com.ruoyi.system.domain.dto.QuestionDto;

import java.util.List;
import java.util.Map;

/**
 * 问题Service接口
 * 
 * @author ruoyi
 * @date 2024-02-02
 */
public interface IQuestionService 
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
     * @param questionDto 问题
     * @param userId 用户id
     * @return 结果
     */
    public Map<String, Object> insertQuestion(QuestionDto questionDto, Long userId);

    /**
     * 修改问题
     * 
     * @param question 问题
     * @return 结果
     */
    public int updateQuestion(Question question);

    /**
     * 批量删除问题
     * 
     * @param ids 需要删除的问题主键集合
     * @return 结果
     */
    public int deleteQuestionByIds(Long[] ids);

    /**
     * 删除问题信息
     * 
     * @param id 问题主键
     * @return 结果
     */
    public int deleteQuestionById(Long id);
}
