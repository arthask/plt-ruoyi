package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.gencode.entity.Flashcard;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 闪卡表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
@Mapper
public interface FlashcardMapper extends BaseMapper<Flashcard> {
    Flashcard getCardOfPackage(@Param("packageUUID") String packageUUID,@Param("offset") Integer offset);
}
