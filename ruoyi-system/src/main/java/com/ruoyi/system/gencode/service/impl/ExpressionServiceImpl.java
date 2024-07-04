package com.ruoyi.system.gencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.domain.dto.expression.ExpressionData;
import com.ruoyi.system.gencode.entity.Expression;
import com.ruoyi.system.gencode.entity.ExpressionDetail;
import com.ruoyi.system.gencode.entity.ExpressionDetailRef;
import com.ruoyi.system.gencode.mapper.ExpressionDetailMapper;
import com.ruoyi.system.gencode.mapper.ExpressionMapper;
import com.ruoyi.system.gencode.service.ExpressionDetailRefService;
import com.ruoyi.system.gencode.service.ExpressionDetailService;
import com.ruoyi.system.gencode.service.ExpressionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 表达记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@Service
public class ExpressionServiceImpl extends ServiceImpl<ExpressionMapper, Expression> implements ExpressionService {
    @Autowired
    private ExpressionDetailService expressionDetailService;

    @Autowired
    private ExpressionDetailRefService expressionDetailRefService;

    @Autowired
    private ExpressionMapper expressionMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addExpressionData(ExpressionData expressionData) {
        String expressionUUID = expressionData.getUuid();;
        if (StringUtils.isBlank(expressionData.getUuid())) {
            expressionUUID = UUID.randomUUID().toString().replace("-", "");
            Expression expression = new Expression();
            expression.setUuid(expressionUUID)
                    .setContent(expressionData.getContent())
                    .setUserId(expressionData.getUserId());
            this.save(expression);
        }
        // 处理表达详情
        if (!CollectionUtils.isEmpty(expressionData.getExpressionDetailData())) {
            List<ExpressionDetail> expressionDetails = new ArrayList<>();
            expressionData.getExpressionDetailData().forEach(e ->  {
                ExpressionDetail expressionDetail = new ExpressionDetail();
                String detailUUID = UUID.randomUUID().toString().replace("-", "");
                expressionDetail.setUuid(detailUUID)
                        .setUserId(expressionData.getUserId())
                        .setContentDetail(e.getContent());
                expressionDetails.add(expressionDetail);
            });
            expressionDetailService.saveBatch(expressionDetails);
            // 新增关系记录
            List<ExpressionDetailRef> refs = new ArrayList<>();
            String finalExpressionUUID = expressionUUID;
            expressionDetails.forEach(e -> {
                ExpressionDetailRef expressionDetailRef = new ExpressionDetailRef();
                expressionDetailRef.setUuid(UUID.randomUUID().toString().replace("-", ""))
                        .setExpressionUuid(finalExpressionUUID)
                        .setExpressionDetailUuid(e.getUuid());
                refs.add(expressionDetailRef);
            });
            expressionDetailRefService.saveBatch(refs);
        }
        return true;
    }

    @Override
    public boolean updateExpressionData(ExpressionData expressionData) {
        Expression expression = getExpressionByUUID(expressionData.getUuid());
        if (Objects.isNull(expression)) {
            return false;
        }
        if (!Objects.equals(expressionData.getContent(), expression.getContent())) {
            Expression updateExpression = new Expression();
            updateExpression.setId(expression.getId()).setContent(expressionData.getContent());
            this.updateById(updateExpression);
        }
        return true;
    }

    @Override
    public ExpressionData getInfo(String expressionUUID) {
        // 查询表达记录
        return expressionMapper.getInfo(expressionUUID);
    }

    @Override
    public int removeExpressionData(String[] ids) {
        return 0;
    }

    private Expression getExpressionByUUID(String uuid) {
        QueryWrapper<Expression> expressionQueryWrapper = new QueryWrapper<>();
        expressionQueryWrapper.eq("uuid", uuid);
        return expressionMapper.selectOne(expressionQueryWrapper);
    }
}
