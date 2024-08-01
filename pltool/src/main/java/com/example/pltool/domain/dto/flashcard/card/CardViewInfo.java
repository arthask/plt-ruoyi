package com.example.pltool.domain.dto.flashcard.card;

import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.Flashcard;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardViewInfo extends Flashcard {
    /**
     * 一张闪卡，可以被多个卡包所共用
     */
    private List<PackageInfoDto> packageInfoDtoList;

    /**
     * 闪卡关联的单词集信息
     */
    private List<WordCollectionData> wordCollectionDataList;

}
