package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.pltool.domain.entity.Label;
import com.example.pltool.mapper.LabelMapper;
import com.example.pltool.service.LabelService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
