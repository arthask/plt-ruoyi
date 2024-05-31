package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.domain.dto.label.LabelInfo;
import com.ruoyi.system.gencode.entity.LabelRef;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 标签关联表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Mapper
public interface LabelRefMapper extends BaseMapper<LabelRef> {
    List<LabelInfo> getLabelInfoByRefUUID(@Param("refUUID") String refUUID);
}
