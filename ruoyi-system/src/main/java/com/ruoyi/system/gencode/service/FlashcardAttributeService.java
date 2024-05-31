package com.ruoyi.system.gencode.service;

import com.ruoyi.system.gencode.entity.FlashcardAttribute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

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
    Map<Integer, Long> getClassifyCount(String packageUUID);


}
