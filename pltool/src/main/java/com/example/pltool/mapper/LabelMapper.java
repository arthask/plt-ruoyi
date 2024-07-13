package com.example.pltool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.pltool.domain.entity.Label;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

}
