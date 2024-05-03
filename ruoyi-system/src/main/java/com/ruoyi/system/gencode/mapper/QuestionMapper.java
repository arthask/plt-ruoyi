package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.gencode.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 问题表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    int updateQuestionBatch(@Param("list") List<Question> questions);
}
