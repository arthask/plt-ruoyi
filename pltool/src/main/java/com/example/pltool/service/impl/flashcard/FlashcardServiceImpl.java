package com.example.pltool.service.impl.flashcard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.flashcard.card.AddCardDto;
import com.example.pltool.domain.dto.flashcard.card.CardInfo;

import com.example.pltool.domain.dto.flashcard.card.PackageCardInfo;
import com.example.pltool.domain.dto.flashcard.cardpackage.OperateWordInCollection;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageCollectionData;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.Flashcard;
import com.example.pltool.domain.entity.PackageCardRef;
import com.example.pltool.domain.entity.Question;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.mapper.FlashcardMapper;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.QuestionService;
import com.example.pltool.service.flashcard.CreateCardService;
import com.example.pltool.service.flashcard.FlashcardService;
import com.example.pltool.service.language.WordCollectionService;
import com.example.pltool.service.language.WordService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

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

    @Resource(name = "wordCardCreator")
    private CreateCardService<Word> wordCardCreator;



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
    public Boolean update(Flashcard flashcard, Long userId) {
        QueryWrapper<Flashcard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", flashcard.getUuid());
        Flashcard updateCard = new Flashcard();
        updateCard.setFront(flashcard.getFront());
        updateCard.setBack(flashcard.getBack());
        updateCard.setUpdateTime(LocalDateTime.now());
        return update(updateCard, queryWrapper);
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


    @Override
    public AjaxResult batchAddCard(PackageCollectionData packageCollectionData) {
        // 卡片去重
        // 根据类型 构造卡片内容
        if (packageCollectionData.getCardType() == 1) {
            // 单词类型卡片
            List<Word> wordsOfCollection = wordCollectionService.getWordsOfCollection(packageCollectionData.getCollectionUUID());
            OperateWordInCollection operateWordInCollection = new OperateWordInCollection();
            operateWordInCollection.setCollectionUUID(packageCollectionData.getCollectionUUID());
            operateWordInCollection.setUserId(packageCollectionData.getUserId());
            operateWordInCollection.setPackageUUIdList(Collections.singletonList(packageCollectionData.getPackageUUID()));
            return wordCardCreator.createCardAndRelationShip(operateWordInCollection, wordsOfCollection);
        }
        return AjaxResult.success(true);
    }

    @Override
    public List<PackageInfoDto> getPackageInfoOfCard(String cardUUId) {
        return packageCardRefService.getPackagesByCardUUId(cardUUId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult delete(String uuid) {
        // 删除关联关系
        QueryWrapper<PackageCardRef> cardRefQueryWrapper = new QueryWrapper<>();
        cardRefQueryWrapper.eq("card_uuid", uuid);
        packageCardRefService.remove(cardRefQueryWrapper);
        // 删除卡片
        QueryWrapper<Flashcard> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        remove(queryWrapper);
        return AjaxResult.success(true);
    }

    @Override
    public AjaxResult addCardsToPackage(PackageCardInfo packageCardInfo) {
        List<PackageCardRef> needToAddList = new ArrayList<>();
        // todo 需要校验是否有重复添加，过滤重添加的单词
        for (String cardUUId : packageCardInfo.getCardUUIdList()) {
            PackageCardRef packageCardRef = new PackageCardRef();
            packageCardRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
            packageCardRef.setCardUuid(cardUUId);
            packageCardRef.setPackageUuid(packageCardInfo.getPackageUUId());
            packageCardRef.setCreateUserId(packageCardInfo.getUserId());
            needToAddList.add(packageCardRef);
        }
        if (!CollectionUtils.isEmpty(needToAddList)) {
            packageCardRefService.saveBatch(needToAddList);
        }
        return AjaxResult.success(true);
    }
}
