package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.mapper.LabelMapper;
import com.example.pltool.service.LabelService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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
        List<Label> labelList = list(queryWrapper);
        if (CollectionUtils.isEmpty(labelList)) {
            return Collections.emptyList();
        }
        return labelList.stream()
                .map(e -> {
                    LabelInfo labelInfo = new LabelInfo();
                    BeanUtils.copyProperties(e, labelInfo);
                    return labelInfo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean createLabel(LabelInfo labelInfo) {
        Label label = new Label();
        label.setUuid(UUID.randomUUID().toString().replace("-", ""));
        BeanUtils.copyProperties(labelInfo, label);
        return save(label);
    }

    @Override
    public boolean updateLabel(LabelInfo labelInfo) {
        Label label = new Label();
        label.setName(label.getName());
        if (Objects.nonNull(label.getTarget())) {
            label.setTarget(label.getTarget());
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
