package com.ruoyi.system.gencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.gencode.entity.FlashcardAttribute;
import com.ruoyi.system.gencode.mapper.FlashcardAttributeMapper;
import com.ruoyi.system.gencode.service.FlashcardAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 闪卡表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@Service
public class FlashcardAttributeServiceImpl extends ServiceImpl<FlashcardAttributeMapper, FlashcardAttribute> implements FlashcardAttributeService {
    @Autowired
    private FlashcardAttributeMapper flashcardAttributeMapper;

    @Override
    public void studyCard(String cardUUID, Integer familiarity) {
        // 标记卡片
        FlashcardAttribute flashcardAttribute = new FlashcardAttribute();
        flashcardAttribute.setCardUuid(cardUUID);
        flashcardAttribute.setFamiliarity(familiarity);
        flashcardAttribute.setUuid(UUID.randomUUID().toString().replace("-",""));
        save(flashcardAttribute);
    }

    @Override
    public Map<Integer, Long> getClassifyCount(String packageUUID) {
        return flashcardAttributeMapper.getClassifyCount(packageUUID);
    }
}
