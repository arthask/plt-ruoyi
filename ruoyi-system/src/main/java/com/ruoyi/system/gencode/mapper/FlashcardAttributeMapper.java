package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.gencode.entity.FlashcardAttribute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 闪卡表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@Mapper
public interface FlashcardAttributeMapper extends BaseMapper<FlashcardAttribute> {
    /**
     * 获取卡包中各熟悉类型数量
     * @param packageUUID packageUUID
     * @return
     */
    Map<Integer, Long>  getClassifyCount(@Param("packageUUID") String packageUUID);
}
