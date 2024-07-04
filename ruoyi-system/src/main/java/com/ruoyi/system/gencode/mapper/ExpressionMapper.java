package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.domain.dto.expression.ExpressionData;
import com.ruoyi.system.gencode.entity.Expression;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
