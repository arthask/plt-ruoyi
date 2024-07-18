package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.controller.business.constant.enums.CardTypeEnum;
import com.example.pltool.controller.business.constant.enums.RefTypeEnum;
import com.example.pltool.domain.dto.flashcard.card.AddCardDto;
import com.example.pltool.domain.dto.flashcard.card.CardInfo;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageCollectionData;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.*;
import com.example.pltool.mapper.FlashcardMapper;
import com.example.pltool.service.FlashcardService;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.QuestionService;
import com.example.pltool.service.language.WordCollectionService;
import com.example.pltool.service.language.WordService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private WordCollectionService wordCollectionService;

    @Autowired
    private LabelRefService labelRefService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addCard(AddCardDto addCardDto, Long userId) {
        Flashcard flashcard = new Flashcard();
        flashcard.setUserId(userId);
        String flashcardUUUID = UUID.randomUUID().toString().replace("-", "");
        // 根据类型 构造卡片内容
        if (addCardDto.getType() == 1) {
            // 单词类型卡片
            String wordUUID = addCardDto.getWordUUID();
            Word word = wordService.getWordByUUID(wordUUID);
            if (Objects.isNull(word)) {
                return Boolean.FALSE;
            }
            flashcard.setUuid(flashcardUUUID);
            flashcard.setFront(word.getWord());
            flashcard.setBack(word.getTranslation());
            flashcard.setType(addCardDto.getType());
            flashcard.setSourceUuid(wordUUID);
        }
        if (addCardDto.getType() == 2) {
            // 问题类型卡片
            String questionUUID = addCardDto.getQuestionUUID();
            Question question = questionService.getByUUID(questionUUID);
            if (Objects.isNull(question)) {
                return Boolean.FALSE;
            }
            flashcard.setUuid(flashcardUUUID);
            flashcard.setFront(question.getQuestion());
            flashcard.setBack(question.getAnswer());
            flashcard.setType(addCardDto.getType());
            flashcard.setSourceUuid(questionUUID);
        }
        save(flashcard);
        if (StringUtils.isNotBlank(addCardDto.getPackageUUID())) {
            PackageCardRef packageCardRef = new PackageCardRef();
            packageCardRef.setCardUuid(flashcardUUUID);
            packageCardRef.setPackageUuid(addCardDto.getPackageUUID());
            packageCardRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult batchAddCard(PackageCollectionData packageCollectionData) {

        List<Flashcard> flashcards = new ArrayList<>();
        List<PackageCardRef> packageCardRefs = new ArrayList<>();
        // 卡片去重
        // 根据类型 构造卡片内容
        if (packageCollectionData.getCardType() == 1) {
            // 单词类型卡片
            List<Word> wordsOfCollection = wordCollectionService.getWordsOfCollection(packageCollectionData.getCollectionUUID());
            wordsOfCollection.stream().filter(Objects::nonNull).forEach(word -> {
                Flashcard flashcard = new Flashcard();
                flashcard.setUserId(packageCollectionData.getUserId());
                String flashcardUUUID = UUID.randomUUID().toString().replace("-", "");
                flashcard.setUuid(flashcardUUUID);
                flashcard.setFront(word.getWord());
                flashcard.setBack(word.getTranslation());
                flashcard.setType(CardTypeEnum.WORD.getValue());
                flashcard.setSourceUuid(word.getUuid());
                flashcard.setCollectionUuid(packageCollectionData.getCollectionUUID());
                flashcards.add(flashcard);
                if (StringUtils.isNotBlank(packageCollectionData.getPackageUUID())) {
                    PackageCardRef packageCardRef = new PackageCardRef();
                    packageCardRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
                    packageCardRef.setCardUuid(flashcardUUUID);
                    packageCardRef.setPackageUuid(packageCollectionData.getPackageUUID());
                    packageCardRefs.add(packageCardRef);
                }
            });
            // 单词集与卡包关联关系去重处理
            // 添加单词集与卡包的关联关系
            LabelRef labelRef = new LabelRef();
            labelRef.setUuid(UUID.randomUUID().toString().replace("-", ""))
                    .setLabelUuid(packageCollectionData.getCollectionUUID())
                    .setRefUuid(packageCollectionData.getPackageUUID())
                    .setRefType(RefTypeEnum.WORD_COLLECTION_OF_PACKAGE.getValue())
                    .setCreateUserId(packageCollectionData.getUserId());
            labelRefService.save(labelRef);
        }
        if (!CollectionUtils.isEmpty(flashcards)) {
            saveBatch(flashcards);
        }
        if (!CollectionUtils.isEmpty(packageCardRefs)) {
            packageCardRefService.saveBatch(packageCardRefs);
        }

        return AjaxResult.success(true);
    }
}
