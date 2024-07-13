package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.flashcard.cardattribute.ClassifyCountVo;
import com.example.pltool.domain.entity.FlashcardAttribute;
import com.example.pltool.mapper.FlashcardAttributeMapper;
import com.example.pltool.service.FlashcardAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<ClassifyCountVo> getClassifyCount(String packageUUID) {
        List<ClassifyCountVo> classifyCountVos = flashcardAttributeMapper.getClassifyCount(packageUUID);
        List<ClassifyCountVo> result = new ArrayList<>();
        for (int i =0 ; i < 3; i++) {
            ClassifyCountVo classifyCountVo = new ClassifyCountVo();
            classifyCountVo.setCount(0L);
            classifyCountVo.setFamiliarity(i);
            classifyCountVo.setNameByFamiliarity(i);
            result.add(classifyCountVo);
        }
        if (CollectionUtils.isEmpty(classifyCountVos)) {
            return result;
        }
        Map<Integer, ClassifyCountVo> classifyCountVoMap = result.stream()
                .collect(Collectors.toMap(ClassifyCountVo::getFamiliarity, Function.identity()));

        classifyCountVos.forEach(e -> {
            if (classifyCountVoMap.containsKey(e.getFamiliarity())) {
                ClassifyCountVo classifyCountVo = classifyCountVoMap.get(e.getFamiliarity());
                classifyCountVo.setCount(e.getCount());
            }
        });
        return result;
    }
}
