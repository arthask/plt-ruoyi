package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.note.QuestionDto;
import com.example.pltool.domain.entity.Question;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问题表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
public interface QuestionService extends IService<Question> {
    /**
     * 新增问题
     *
     * @param questionDto 问题
     * @param userId      用户id
     * @return 结果
     */
    Map<String, Object> insertQuestion(QuestionDto questionDto, Long userId);

    /**
     * 使用uuid获取
     * @param uuid uuid
     * @return
     */
    Question getByUUID(String uuid);

    /**
     * 搜索问题
     * @param question 问题
     * @return
     */
    List<Question>  searchQuestion(String question);
}
