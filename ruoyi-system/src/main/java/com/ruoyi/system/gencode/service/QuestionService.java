package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.QuestionDto;
import com.ruoyi.system.gencode.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;

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
