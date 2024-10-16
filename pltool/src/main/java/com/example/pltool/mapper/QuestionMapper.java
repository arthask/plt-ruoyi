package com.example.pltool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Question;

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
