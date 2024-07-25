package com.example.pltool.service.flashcard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.flashcard.cardattribute.ClassifyCountVo;
import com.example.pltool.domain.entity.FlashcardAttribute;


import java.util.List;

/**
 * <p>
 * 闪卡表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
public interface FlashcardAttributeService extends IService<FlashcardAttribute> {

    /**
     * 学习闪卡
     * @param cardUUID 闪卡uuid
     * @param familiarity 熟悉程度
     */
    void studyCard(String cardUUID, Integer familiarity);

    /**
     * 获取分类数量
     * @return
     */
    List<ClassifyCountVo> getClassifyCount(String packageUUID);


}
