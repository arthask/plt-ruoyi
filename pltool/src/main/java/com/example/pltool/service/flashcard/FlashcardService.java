package com.example.pltool.service.flashcard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.flashcard.card.*;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageCollectionData;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.Flashcard;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;


/**
 * <p>
 * 闪卡表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
public interface FlashcardService extends IService<Flashcard> {
    /**
     * 添加卡片
     * @param addCardDto
     * @param userId
     * @return
     */
    Boolean addCard(AddCardDto addCardDto, Long userId);

    /**
     * 批量创建卡片
     *
     * @return
     */
    AjaxResult batchAddCard(BatchAddCardDto batchAddCardDto);

    /**
     * 添加卡片
     * @param flashcard
     * @param userId
     * @return
     */
    Boolean update(Flashcard flashcard, Long userId);

    /**
     * 获取卡包中的一张卡片
     * @param packageUUID 卡包uuid
     * @return
     */
    CardInfo getCardOfPackage(String packageUUID, Integer offset);

    List<Flashcard> getCardListInPackage(String packageUUId);

    /**
     * 通过uuid获取卡片
     * @param cardUUID 卡片uuid
     * @return
     */
    Flashcard getCardByUUId(String cardUUID);

    CardInfo searchClassifyCard(String packageUUID,Integer type, Integer offset);

    /**
     * 使用单词集添加闪卡
     * @param packageCollectionData
     * @return
     */
    AjaxResult batchAddCardByWordCollection(PackageCollectionData packageCollectionData);

    /**
     * 获取和卡片关联的卡包信息
     * @param cardUUId
     * @return
     */
    List<PackageInfoDto> getPackageInfoOfCard(String cardUUId);

    /**
     * 删除
     * @param uuid
     * @return
     */
    AjaxResult delete(String uuid);

    AjaxResult addCardsToPackage(PackageCardInfo packageCardInfo);

    AjaxResult cancelAssociation2Package(CancelAssociationDto cancelAssociationDto);
}
