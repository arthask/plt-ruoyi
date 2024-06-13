package com.ruoyi.system.gencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.system.gencode.entity.Flashcard;
import com.ruoyi.system.gencode.entity.FlashcardAttribute;
import com.ruoyi.system.gencode.entity.Question;
import com.ruoyi.system.gencode.mapper.FlashcardAttributeMapper;
import com.ruoyi.system.gencode.service.FlashcardAttributeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.gencode.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private FlashcardService flashcardService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void studyCard(String cardUUID, Integer familiarity) {
        QueryWrapper<FlashcardAttribute> flashcardAttributeQueryWrapper = new QueryWrapper<>();
        flashcardAttributeQueryWrapper.eq("card_uuid", cardUUID);
        FlashcardAttribute attribute = getOne(flashcardAttributeQueryWrapper);
        if (Objects.isNull(attribute)) {
            // 标记卡片
            FlashcardAttribute flashcardAttribute = new FlashcardAttribute();
            flashcardAttribute.setCardUuid(cardUUID);
            flashcardAttribute.setFamiliarity(familiarity);
            flashcardAttribute.setUuid(UUID.randomUUID().toString().replace("-",""));
            save(flashcardAttribute);
        } else {
            // 修改熟练类型
            FlashcardAttribute flashcardAttribute = new FlashcardAttribute();
            flashcardAttribute.setId(attribute.getId());
            flashcardAttribute.setFamiliarity(familiarity);
            flashcardAttribute.setUpdateTime(LocalDateTime.now());
            updateById(flashcardAttribute);
        }
    }

    @Override
    public List<Map<Integer, Long>> getClassifyCount(String packageUUID) {
        return flashcardAttributeMapper.getClassifyCount(packageUUID);
    }
}
