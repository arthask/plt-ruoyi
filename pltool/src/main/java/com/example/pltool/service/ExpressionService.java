package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.expression.ExpressionData;
import com.example.pltool.domain.entity.Expression;

import java.util.List;


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

    boolean batchAddExpression(ExpressionData expressionData);

    boolean updateExpressionData(ExpressionData expressionData);

    ExpressionData getInfo(String expressionUUID);

    boolean removeExpressionData(List<String> uuidList);

    List<ExpressionData> getExpressionListByUUIds(List<String> uuidList);
}
