package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.mapper.LabelRefMapper;
import com.example.pltool.service.LabelRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标签关联表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Service
public class LabelRefServiceImpl extends ServiceImpl<LabelRefMapper, LabelRef> implements LabelRefService {
    @Autowired
    private LabelRefMapper labelRefMapper;
    @Override
    public List<LabelInfo> getLabelInfoByRefUUID(String refUUID) {
        return labelRefMapper.getLabelInfoByRefUUID(refUUID);
    }
}
