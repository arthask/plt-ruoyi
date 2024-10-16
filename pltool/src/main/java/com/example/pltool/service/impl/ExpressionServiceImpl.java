package com.example.pltool.service.impl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.expression.BatchAddExpressionDto;
import com.example.pltool.domain.dto.expression.ExpressionData;
import com.example.pltool.domain.dto.expression.ExpressionDetailData;
import com.example.pltool.domain.entity.Expression;
import com.example.pltool.domain.entity.ExpressionDetail;
import com.example.pltool.domain.entity.ExpressionDetailRef;
import com.example.pltool.mapper.ExpressionMapper;
import com.example.pltool.service.ExpressionDetailRefService;
import com.example.pltool.service.ExpressionDetailService;
import com.example.pltool.service.ExpressionService;
import com.ruoyi.common.utils.StringUtils;

/**
 * <p>
 * 表达记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@Service
public class ExpressionServiceImpl extends ServiceImpl<ExpressionMapper, Expression>
    implements ExpressionService {
  @Autowired
  private ExpressionDetailService expressionDetailService;

  @Autowired
  private ExpressionDetailRefService expressionDetailRefService;


  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean addExpressionData(ExpressionData expressionData) {
    String expressionUUID = expressionData.getUuid();
    if (StringUtils.isBlank(expressionData.getUuid())) {
      expressionUUID = UUID.randomUUID().toString().replace("-", "");
      Expression expression = new Expression();
      expression.setUuid(expressionUUID).setContent(expressionData.getContent())
          .setUserId(expressionData.getUserId());
      this.save(expression);
    }
    // 处理表达详情
    if (!CollectionUtils.isEmpty(expressionData.getExpressionDetailData())) {
      List<ExpressionDetail> expressionDetails = new ArrayList<>();
      expressionData.getExpressionDetailData().forEach(e -> {
        ExpressionDetail expressionDetail = new ExpressionDetail();
        String detailUUID = UUID.randomUUID().toString().replace("-", "");
        expressionDetail.setUuid(detailUUID).setUserId(expressionData.getUserId())
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
            .setExpressionUuid(finalExpressionUUID).setExpressionDetailUuid(e.getUuid());
        refs.add(expressionDetailRef);
      });
      expressionDetailRefService.saveBatch(refs);
    }
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean batchAddExpression(ExpressionData expressionData) {
    if (CollectionUtils.isEmpty(expressionData.getBatchAddExpressionDtoList())) {
      return false;
    }
    List<Expression> neddAddExpressionList = new ArrayList<>();
    List<ExpressionDetail> expressionDetails = new ArrayList<>();
    List<ExpressionDetailRef> expressionDetailRefs = new ArrayList<>();
    Map<String, String> uuidMap = new HashMap<>();
    for (BatchAddExpressionDto item : expressionData.getBatchAddExpressionDtoList()) {
      String expressionUUID = UUID.randomUUID().toString().replace("-", "");
      Expression expression = new Expression();
      expression.setUuid(expressionUUID).setContent(item.getContent())
          .setUserId(expressionData.getUserId());
      neddAddExpressionList.add(expression);

      ExpressionDetail expressionDetail = new ExpressionDetail();
      String detailUUID = UUID.randomUUID().toString().replace("-", "");
      expressionDetail.setUuid(detailUUID).setUserId(expressionData.getUserId())
          .setContentDetail(item.getExpression());
      expressionDetails.add(expressionDetail);
      uuidMap.put(expressionUUID, detailUUID);
    }
    if (!CollectionUtils.isEmpty(neddAddExpressionList)) {
      this.saveBatch(neddAddExpressionList);
    }
    if (!CollectionUtils.isEmpty(expressionDetails)) {
      expressionDetailService.saveBatch(expressionDetails);
    }
    if (!CollectionUtils.isEmpty(uuidMap)) {
      uuidMap.forEach((k, v) -> {
        ExpressionDetailRef expressionDetailRef = new ExpressionDetailRef();
        expressionDetailRef.setUuid(UUID.randomUUID().toString().replace("-", ""))
            .setExpressionUuid(k).setExpressionDetailUuid(v);
        expressionDetailRefs.add(expressionDetailRef);
      });
      expressionDetailRefService.saveBatch(expressionDetailRefs);
    }
    return true;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean updateExpressionData(ExpressionData expressionData) {
    Expression expression = getExpressionByUUID(expressionData.getUuid());
    if (Objects.isNull(expression)) {
      return false;
    }
    if (!Objects.equals(expressionData.getContent(), expression.getContent())) {
      Expression updateExpression = new Expression();
      updateExpression.setId(expression.getId()).setContent(expressionData.getContent())
          .setUpdateTime(LocalDateTime.now());
      this.updateById(updateExpression);
    }
    Map<String, ExpressionDetailData> expressionDetailDataMap =
        expressionData.getExpressionDetailData().stream()
            .collect(Collectors.toMap(ExpressionDetailData::getUuid, Function.identity()));
    if (CollectionUtils.isEmpty(expressionDetailDataMap)) {
      return false;
    }
    List<ExpressionDetail> detailList =
        getExpressionDetailListByUUID(new ArrayList<>(expressionDetailDataMap.keySet()));
    if (CollectionUtils.isEmpty(detailList)) {
      return false;
    }
    List<ExpressionDetail> updateDetailList = new ArrayList<>();
    detailList.forEach(e -> {
      if (expressionDetailDataMap.containsKey(e.getUuid())) {
        ExpressionDetailData expressionDetailData = expressionDetailDataMap.get(e.getUuid());
        if (!Objects.equals(e.getContentDetail(), expressionDetailData.getContent())) {
          ExpressionDetail updateExpressionDetail = new ExpressionDetail();
          updateExpressionDetail.setId(e.getId())
              .setContentDetail(expressionDetailData.getContent())
              .setUpdateTime(LocalDateTime.now());
          updateDetailList.add(updateExpressionDetail);
        }
      }
    });
    if (!CollectionUtils.isEmpty(updateDetailList)) {
      expressionDetailService.updateBatchById(updateDetailList);
    }
    return true;
  }

  @Override
  public ExpressionData getInfo(String expressionUUID) {
    // 查询表达记录
    return getBaseMapper().getInfo(expressionUUID);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public boolean removeExpressionData(List<String> uuidList) {
    LambdaQueryWrapper<ExpressionDetailRef> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.in(ExpressionDetailRef::getExpressionUuid, uuidList);
    List<ExpressionDetailRef> expressionDetailRefList =
        expressionDetailRefService.list(queryWrapper);
    if (CollectionUtils.isEmpty(expressionDetailRefList)) {
      return true;
    }
    List<String> expressionDetialUUIdList = expressionDetailRefList.stream()
        .map(ExpressionDetailRef::getExpressionDetailUuid).collect(Collectors.toList());
    LambdaQueryWrapper<Expression> expressionQueryWrapper = new LambdaQueryWrapper<>();
    expressionQueryWrapper.in(Expression::getUuid, uuidList);
    remove(expressionQueryWrapper);
    LambdaQueryWrapper<ExpressionDetail> expressionDetailQueryWrapper = new LambdaQueryWrapper<>();
    expressionDetailQueryWrapper.in(ExpressionDetail::getUuid, expressionDetialUUIdList);
    expressionDetailService.remove(expressionDetailQueryWrapper);
    expressionDetailRefService.remove(queryWrapper);
    return true;
  }

  @Override
  public List<ExpressionData> getExpressionListByUUIds(List<String> uuidList) {
    if (CollectionUtils.isEmpty(uuidList)) {
      return Collections.emptyList();
    }
    return this.getBaseMapper().getExpressionInfoList(uuidList);
  }

  private Expression getExpressionByUUID(String uuid) {
    QueryWrapper<Expression> expressionQueryWrapper = new QueryWrapper<>();
    expressionQueryWrapper.eq("uuid", uuid);
    return getBaseMapper().selectOne(expressionQueryWrapper);
  }

  private List<ExpressionDetail> getExpressionDetailListByUUID(List<String> uuids) {
    QueryWrapper<ExpressionDetail> queryWrapper = new QueryWrapper<>();
    queryWrapper.in("uuid", uuids);
    return expressionDetailService.getBaseMapper().selectList(queryWrapper);
  }
}
