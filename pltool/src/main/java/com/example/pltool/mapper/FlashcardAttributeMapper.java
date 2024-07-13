package com.example.pltool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.pltool.domain.dto.flashcard.cardattribute.ClassifyCountVo;
import com.example.pltool.domain.entity.FlashcardAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<ClassifyCountVo> getClassifyCount(@Param("packageUUID") String packageUUID);
}
