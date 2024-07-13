package com.example.pltool.service.language;

import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.Word;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

public interface WordCollectionService {
    boolean addWordCollectionData(WordCollectionData wordCollectionData);

    /**
     * 更新单词集信息
     * @param wordCollectionData
     * @return
     */
    AjaxResult updateWordCollectionData(WordCollectionData wordCollectionData);

    /**
     * 获取单词集信息
     * @param uuid
     * @return
     */
    WordCollectionData getInfo(String uuid);

    /**
     * 获取所有的标签即单词集
     * @return
     */
    List<WordCollectionData> getAllWordCollection();

    /**
     * 获取单词集中的单词
     * @param labelUUID
     * @return
     */
    List<Word> getWordsOfCollection(String labelUUID);

    /**
     * 往单词集中添加单词
     * @param wordCollectionData 单词集数据
     * @return
     */
    AjaxResult addWordToCollection(WordCollectionData wordCollectionData);
}
