package com.ruoyi.system.domain.dto.expression;

import lombok.Data;

import java.util.List;

@Data
public class ExpressionData {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 表达内容
     */
    private String content;

    /**
     * 具体表达详情
     */
    private List<ExpressionDetailData> expressionDetailData;
}
