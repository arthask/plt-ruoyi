package com.example.pltool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.dto.expression.ExpressionData;
import com.example.pltool.domain.entity.Expression;

/**
 * <p>
 * 表达记录表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@Mapper
public interface ExpressionMapper extends BaseMapper<Expression> {
  ExpressionData getInfo(@Param("expressionUUID") String expressionUUID);

  List<ExpressionData> getExpressionInfoList(
      @Param("expressionUUIdList") List<String> expressionUUIdList);
}
