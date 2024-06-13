package com.ruoyi.system.gencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.dto.flashcard.card.AddCardDto;
import com.ruoyi.system.domain.dto.flashcard.card.CardInfo;
import com.ruoyi.system.gencode.entity.Flashcard;
import com.ruoyi.system.gencode.entity.PackageCardRef;
import com.ruoyi.system.gencode.entity.Question;
import com.ruoyi.system.gencode.entity.Word;
import com.ruoyi.system.gencode.mapper.FlashcardMapper;
import com.ruoyi.system.gencode.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 闪卡表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
@Service
public class FlashcardServiceImpl extends ServiceImpl<FlashcardMapper, Flashcard> implements FlashcardService {
    @Autowired
    private WordService wordService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PackageCardRefService packageCardRefService;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addCard(AddCardDto addCardDto, Long userId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setUserId(userId);
        String flashcardUUUID = UUID.randomUUID().toString().replace("-","");
        // 根据类型 构造卡片内容
        if(addCardDto.getType() == 1) {
            // 单词类型卡片
            String wordUUID = addCardDto.getWordUUID();
            Word word = wordService.getWordByUUID(wordUUID);
            if(Objects.isNull(word)) {
                return Boolean.FALSE;
            }
            flashcard.setUuid(flashcardUUUID);
            flashcard.setFront(word.getWord());
            flashcard.setBack(word.getTranslation());
            flashcard.setType(addCardDto.getType());
        }
        if (addCardDto.getType() == 2) {
            // 问题类型卡片
            String questionUUID = addCardDto.getQuestionUUID();
            Question question = questionService.getByUUID(questionUUID);
            if(Objects.isNull(question)) {
                return Boolean.FALSE;
            }
            flashcard.setUuid(flashcardUUUID);
            flashcard.setFront(question.getQuestion());
            flashcard.setBack(question.getAnswer());
            flashcard.setType(addCardDto.getType());
        }
        save(flashcard);
        if (StringUtils.isNotBlank(addCardDto.getPackageUUID())) {
            PackageCardRef packageCardRef = new PackageCardRef();
            packageCardRef.setCardUuid(flashcardUUUID);
            packageCardRef.setPackageUuid(addCardDto.getPackageUUID());
            packageCardRef.setUuid(UUID.randomUUID().toString().replace("-",""));
            packageCardRefService.save(packageCardRef);
        }
        return Boolean.TRUE;
    }

    @Override
    public Flashcard getCardOfPackage(String packageUUID,Integer offset) {
        return flashcardMapper.getCardOfPackage(packageUUID, offset);
    }

    @Override
    public Flashcard getCardByUUId(String cardUUID) {
        QueryWrapper<Flashcard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", cardUUID);
        return getOne(queryWrapper);
    }
}
