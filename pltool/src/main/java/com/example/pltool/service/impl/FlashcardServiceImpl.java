package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.flashcard.card.AddCardDto;
import com.example.pltool.domain.dto.flashcard.card.CardInfo;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.Flashcard;
import com.example.pltool.domain.entity.PackageCardRef;
import com.example.pltool.domain.entity.Question;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.mapper.FlashcardMapper;
import com.example.pltool.service.FlashcardService;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.QuestionService;
import com.example.pltool.service.language.WordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
    public CardInfo getCardOfPackage(String packageUUID, Integer offset) {
        CardInfo cardInfo = new CardInfo();
        Flashcard cardOfPackage = flashcardMapper.getCardOfPackage(packageUUID, offset);
        if (Objects.nonNull(cardOfPackage)) {
            BeanUtils.copyProperties(cardOfPackage, cardInfo);
        }
        Long cardCountOfPackage = flashcardMapper.getCardCountOfPackage(packageUUID);
        PackageInfoDto packageInfoDto = new PackageInfoDto();
        packageInfoDto.setCardCount(cardCountOfPackage);
        cardInfo.setPackageInfoDto(packageInfoDto);
        return cardInfo;
    }

    @Override
    public Flashcard getCardByUUId(String cardUUID) {
        QueryWrapper<Flashcard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", cardUUID);
        return getOne(queryWrapper);
    }

    @Override
    public CardInfo searchClassifyCard(String packageUUID, Integer type, Integer offset) {
        CardInfo cardInfo = new CardInfo();
        Flashcard cardOfPackage = flashcardMapper.getCardByType(packageUUID, type, offset);
        if (Objects.nonNull(cardOfPackage)) {
            BeanUtils.copyProperties(cardOfPackage, cardInfo);
        }
        Long cardCountOfPackage = flashcardMapper.getCardCountByType(packageUUID, type);
        PackageInfoDto packageInfoDto = new PackageInfoDto();
        packageInfoDto.setCardCount(cardCountOfPackage);
        cardInfo.setPackageInfoDto(packageInfoDto);
        return cardInfo;
    }
}
