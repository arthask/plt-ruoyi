package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.expression.ExpressionData;
import com.ruoyi.system.gencode.entity.Expression;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 表达记录表 服务类
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
public interface ExpressionService extends IService<Expression> {

    boolean addExpressionData(ExpressionData expressionData);

    boolean updateExpressionData(ExpressionData expressionData);

    ExpressionData getInfo(String expressionUUID);

    int removeExpressionData(String[] ids);
}
