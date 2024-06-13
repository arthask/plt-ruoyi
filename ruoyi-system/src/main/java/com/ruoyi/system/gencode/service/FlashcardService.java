package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.flashcard.card.AddCardDto;
import com.ruoyi.system.domain.dto.flashcard.card.CardInfo;
import com.ruoyi.system.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.ruoyi.system.gencode.entity.Flashcard;
import com.baomidou.mybatisplus.extension.service.IService;

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
     * 获取卡包中的一张卡片
     * @param packageUUID 卡包uuid
     * @return
     */
    CardInfo getCardOfPackage(String packageUUID,Integer offset);

    /**
     * 通过uuid获取卡片
     * @param cardUUID 卡片uuid
     * @return
     */
    Flashcard getCardByUUId(String cardUUID);

    CardInfo searchClassifyCard(String packageUUID,Integer type, Integer offset);
}
