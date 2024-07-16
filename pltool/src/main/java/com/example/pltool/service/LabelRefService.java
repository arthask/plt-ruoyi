package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.domain.entity.Word;


import java.util.List;

/**
 * <p>
 * 标签关联表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
public interface LabelRefService extends IService<LabelRef> {

    /**
     * 使用关联uuid获取标签信息
     * @param refUUID 关联uuid
     * @return
     */
    List<LabelInfo> getLabelInfoByRefUUID(String refUUID);

    /**
     * 获取所有的标签即单词集
     * @return
     */
    List<WordCollectionData> getAllWordCollection(Integer type);

    /**
     * 获取单词集中的单词详情
     * @param labelUUID
     * @return
     */
    List<Word> getWordsOfCollection(String labelUUID);


    List<Word> getWordsOfCollection(List<String> labelUUIDList);

    List<WordCollectionData> getCollectionsOfPackage(Integer type, String packageUUId, Long userId);

}
