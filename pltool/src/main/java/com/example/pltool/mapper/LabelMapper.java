package com.example.pltool.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Label;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-08-12
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

}
