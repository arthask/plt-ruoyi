package com.example.pltool.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.entity.ExpressionDetail;
import com.example.pltool.mapper.ExpressionDetailMapper;
import com.example.pltool.service.ExpressionDetailService;

/**
 * <p>
 * 表达记录详情表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-07-04
 */
@Service
public class ExpressionDetailServiceImpl extends
    ServiceImpl<ExpressionDetailMapper, ExpressionDetail> implements ExpressionDetailService {
}
