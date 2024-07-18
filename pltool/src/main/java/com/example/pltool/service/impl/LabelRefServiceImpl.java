package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.pltool.controller.business.constant.enums.RefTypeEnum;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.mapper.LabelRefMapper;
import com.example.pltool.service.LabelRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签关联表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Service
public class LabelRefServiceImpl extends ServiceImpl<LabelRefMapper, LabelRef> implements LabelRefService {
    @Autowired
    private LabelRefMapper labelRefMapper;

    @Override
    public List<LabelInfo> getLabelInfoByRefUUID(String refUUID) {
        return labelRefMapper.getLabelInfoByRefUUID(refUUID);
    }

    @Override
    public List<WordCollectionData> getAllWordCollection(Integer type) {
        List<WordCollectionData> allCollectionByType = labelRefMapper.getAllCollectionByType(type);
        // 获取创建时间
        if (CollectionUtils.isEmpty(allCollectionByType)) {
            return Collections.emptyList();
        }
        return setWordCollectionData(type, allCollectionByType);
    }

    @Override
    public List<Word> getWordsOfCollection(String labelUUID) {
        return labelRefMapper.getWordsOfCollection(labelUUID);
    }

    @Override
    public List<Word> getWordsOfCollection(List<String> labelUUIDList) {
        return labelRefMapper.getWordsOfCollectionUUIdList(labelUUIDList);
    }

    @Override
    public List<WordCollectionData> getCollectionsOfPackage(Integer type, String packageUUId, Long userId) {
        List<WordCollectionData> allCollectionByType = labelRefMapper.getCollectionsOfPackage(type, packageUUId, userId);
        // 获取创建时间
        if (CollectionUtils.isEmpty(allCollectionByType)) {
            return Collections.emptyList();
        }
        return setWordCollectionData(type, allCollectionByType);
    }

    @Override
    public boolean isRepeatBindWordCollection(String packageUUId, String collectionUUId, Long userId) {
        // 添加单词集与卡包的关联关系
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_uuid", collectionUUId)
                .eq("ref_uuid", packageUUId)
                .eq("ref_type", RefTypeEnum.WORD_COLLECTION_OF_PACKAGE.getValue())
                .eq("create_user_id", userId);
        return count(queryWrapper) > 0;
    }

    private List<WordCollectionData> setWordCollectionData(Integer type, List<WordCollectionData> allCollectionByType) {
        Map<String, WordCollectionData> collectionDataMap = allCollectionByType.stream()
                .collect(Collectors.toMap(WordCollectionData::getLabelUUID, Function.identity()));

        List<String> labelUUIdList = allCollectionByType.stream()
                .map(WordCollectionData::getLabelUUID)
                .collect(Collectors.toList());
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ref_type", type);
        queryWrapper.in("label_uuid", labelUUIdList);
        List<LabelRef> labelRefs = list(queryWrapper);
        if (CollectionUtils.isEmpty(labelRefs)) {
            return Collections.emptyList();
        }
        Map<String, List<LabelRef>> groupByLabelUUId = labelRefs.stream()
                .collect(Collectors.groupingBy(LabelRef::getLabelUuid));
        groupByLabelUUId.forEach((k, v) -> {
            v.sort(Comparator.comparing(LabelRef::getCreateTime));
            LabelRef labelRef = v.get(0);
            collectionDataMap.get(k).setCreateTime(labelRef.getCreateTime());
        });
        return allCollectionByType;
    }
}
