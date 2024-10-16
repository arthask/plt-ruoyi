package com.example.pltool.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.PackageCardRef;

/**
 * <p>
 * 卡包闪卡关系表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-07-25
 */
@Mapper
public interface PackageCardRefMapper extends BaseMapper<PackageCardRef> {

}
