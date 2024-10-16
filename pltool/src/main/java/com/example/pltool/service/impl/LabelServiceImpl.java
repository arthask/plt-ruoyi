package com.example.pltool.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.controller.business.constant.enums.TargetTypeEnum;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.mapper.LabelMapper;
import com.example.pltool.service.LabelService;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {
  @Override
  public Label getLabelByUUID(String uuid) {
    QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("uuid", uuid);
    return getOne(queryWrapper);
  }

  @Override
  public boolean isDuplicateName(String name) {
    QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("name", name);
    return count(queryWrapper) > 0;
  }

  @Override
  public List<LabelInfo> getAllLabels(Long userId) {
    QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_user_id", userId);
    queryWrapper.eq("target", TargetTypeEnum.WORD.getValue());
    List<Label> labelList = list(queryWrapper);
    if (CollectionUtils.isEmpty(labelList)) {
      return Collections.emptyList();
    }
    return LabelInfo.convertListData2Vo(labelList);
  }

  @Override
  public boolean createLabel(LabelInfo labelInfo) {
    Label label = new Label();
    BeanUtils.copyProperties(labelInfo, label);
    label.setUuid(UUID.randomUUID().toString().replace("-", ""));
    return save(label);
  }

  @Override
  public boolean updateLabel(LabelInfo labelInfo) {
    if (StringUtils.isEmpty(labelInfo.getName()) && Objects.isNull(labelInfo.getTarget())) {
      return false;
    }
    Label label = new Label();
    if (StringUtils.isNotEmpty(labelInfo.getName())) {
      label.setName(labelInfo.getName());
    }
    if (Objects.nonNull(labelInfo.getTarget())) {
      label.setTarget(labelInfo.getTarget());
    }
    QueryWrapper<Label> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("uuid", labelInfo.getUuid());
    return update(label, queryWrapper);
  }

  @Override
  public LabelInfo getLabelInfo(String uuid) {
    Label label = getLabelByUUID(uuid);
    if (Objects.isNull(label)) {
      return null;
    }
    return LabelInfo.convertData2Vo(label);
  }
}
