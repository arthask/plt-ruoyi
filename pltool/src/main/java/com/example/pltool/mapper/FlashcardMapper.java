package com.example.pltool.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Flashcard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 闪卡表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-07-16
 */
@Mapper
public interface FlashcardMapper extends BaseMapper<Flashcard> {
    /**
     * 获取卡包中的一张卡片
     *
     * @param packageUUID 卡包uuid
     * @param offset      第几张卡片，下标从0开始
     * @return
     */
    Flashcard getCardOfPackage(@Param("packageUUID") String packageUUID, @Param("offset") Integer offset);

    /**
     * 获取卡包中的卡片数量
     *
     * @param packageUUID 卡包uuid
     * @return
     */
    Long getCardCountOfPackage(@Param("packageUUID") String packageUUID);

    /**
     * 获取卡包中指定类型的的一张卡片
     *
     * @param packageUUID 卡包uuid
     * @param offset      第几张卡片，下标从0开始
     * @return
     */
    Flashcard getCardByType(@Param("packageUUID") String packageUUID, @Param("type") Integer type,
                            @Param("offset") Integer offset);

    /**
     * 获取卡包中的卡片数量
     *
     * @param packageUUID 卡包uuid
     * @return
     */
    Long getCardCountByType(@Param("packageUUID") String packageUUID, @Param("type") Integer type);
}
