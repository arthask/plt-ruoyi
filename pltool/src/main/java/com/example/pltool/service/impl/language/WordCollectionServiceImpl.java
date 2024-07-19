package com.example.pltool.service.impl.language;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pltool.controller.business.constant.enums.CardTypeEnum;
import com.example.pltool.controller.business.constant.enums.RefTypeEnum;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageCollectionData;
import com.example.pltool.domain.dto.flashcard.cardpackage.RemoveCollectionOfPackage;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.*;
import com.example.pltool.service.FlashcardService;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.LabelService;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.language.WordCollectionService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WordCollectionServiceImpl implements WordCollectionService {
    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private FlashcardService flashcardService;

    @Autowired
    private PackageCardRefService packageCardRefService;

    @Override
    public boolean addWordCollectionData(WordCollectionData wordCollectionData) {
        return false;
    }

    @Override
    public AjaxResult updateWordCollectionData(WordCollectionData wordCollectionData) {
        // 先检查是否重名
        if (labelService.isDuplicateName(wordCollectionData.getName())) {
            return AjaxResult.error("存在重名，请修改后重新提交");
        }
        Label updateLabel = new Label();
        updateLabel.setName(wordCollectionData.getName());
        updateLabel.setUpdateTime(LocalDateTime.now());
        UpdateWrapper<Label> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uuid", wordCollectionData.getLabelUUID());
        labelService.update(updateLabel, updateWrapper);
        return AjaxResult.success(true);
    }

    @Override
    public WordCollectionData getInfo(String uuid) {
        Label label = labelService.getLabelByUUID(uuid);
        WordCollectionData wordCollectionData = new WordCollectionData();
        wordCollectionData.setName(label.getName());
        wordCollectionData.setLabelUUID(label.getUuid());
        return wordCollectionData;
    }

    @Override
    public List<WordCollectionData> getAllWordCollection() {
        return labelRefService.getAllWordCollection(RefTypeEnum.WORD.getValue());
    }

    @Override
    public List<Word> getWordsOfCollection(String labelUUID) {
        return labelRefService.getWordsOfCollection(labelUUID);
    }

    @Override
    public List<WordCollectionData> getCollectionsOfPackage(String packageUUId, Long userId) {
        return labelRefService.getCollectionsOfPackage(RefTypeEnum.WORD_COLLECTION_OF_PACKAGE.getValue()
                , packageUUId, userId);
    }

    @Override
    public AjaxResult addWordToCollection(WordCollectionData wordCollectionData) {
        // 相同单词添加到相同单词集 需要过滤
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ref_type", RefTypeEnum.WORD.getValue());
        queryWrapper.eq("create_user_id", wordCollectionData.getUserId());
        queryWrapper.eq("label_uuid", wordCollectionData.getLabelUUID());
        queryWrapper.in("ref_uuid", wordCollectionData.getWordUUIDList());
        List<LabelRef> labelRefs = labelRefService.list(queryWrapper);
        Map<String, List<LabelRef>> labelWordListMap = labelRefs
                .stream()
                .collect(Collectors.groupingBy(LabelRef::getLabelUuid));

        List<String> filterdWordUUIdList = new ArrayList<>();
        boolean filteredFlag = false;
        if (labelWordListMap.containsKey(wordCollectionData.getLabelUUID())) {
            List<String> existWordUUIdList = labelWordListMap.get(wordCollectionData.getLabelUUID())
                    .stream()
                    .map(LabelRef::getRefUuid)
                    .collect(Collectors.toList());
            filterdWordUUIdList.addAll(wordCollectionData.getWordUUIDList()
                    .stream()
                    .filter(e -> !existWordUUIdList.contains(e))
                    .collect(Collectors.toList()));
        } else {
            filterdWordUUIdList.addAll(wordCollectionData.getWordUUIDList());
        }
        if (filterdWordUUIdList.size() < wordCollectionData.getWordUUIDList().size()) {
            filteredFlag = true;
        }
        List<LabelRef> addLabelRefList = new ArrayList<>();
        // 对单词和标签进行绑定
        filterdWordUUIdList.forEach(e -> {
            LabelRef labelRef = new LabelRef();
            labelRef.setRefType(RefTypeEnum.WORD.getValue());
            labelRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
            labelRef.setLabelUuid(wordCollectionData.getLabelUUID());
            labelRef.setRefUuid(e);
            labelRef.setCreateUserId(wordCollectionData.getUserId());
            addLabelRefList.add(labelRef);
        });
        if (!CollectionUtils.isEmpty(addLabelRefList)) {
            labelRefService.saveBatch(addLabelRefList);
        }
        if (filteredFlag) {
            return AjaxResult.success("已过滤重复添加的单词", true);
        }
        return AjaxResult.success(true);
    }

    @Override
    public List<LabelInfo> getAllLabels(Long userId) {
        return labelService.getAllLabels(userId);
    }

    @Override
    public AjaxResult addCollectionToPackage(PackageCollectionData packageCollectionData) {
        // 过滤掉重复绑定的数据
        boolean repeatBindWordCollection = labelRefService.isRepeatBindWordCollection(packageCollectionData.getPackageUUID(),
                packageCollectionData.getCollectionUUID(), packageCollectionData.getUserId());
        if(repeatBindWordCollection) {
            return AjaxResult.success("该单词集已绑定，请重新选择", true);
        }
        return flashcardService.batchAddCard(packageCollectionData);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult removeCollectionOfPackage(RemoveCollectionOfPackage removeCollectionOfPackage) {
        // 删除卡包与单词集的关联关系
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ref_uuid", removeCollectionOfPackage.getPackageUUId());
        queryWrapper.eq("ref_type", RefTypeEnum.WORD_COLLECTION_OF_PACKAGE.getValue());
        queryWrapper.eq("create_user_id", removeCollectionOfPackage.getUserId());
        queryWrapper.in("label_uuid", removeCollectionOfPackage.getCollectionUUIdList());
        labelRefService.remove(queryWrapper);
        // 查找单词集下单词对应的卡片uuid集合
        QueryWrapper<LabelRef> cardQueryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ref_type", RefTypeEnum.CARD_COLLECTION.getValue());
        queryWrapper.eq("create_user_id", removeCollectionOfPackage.getUserId());
        queryWrapper.in("label_uuid", removeCollectionOfPackage.getCollectionUUIdList());
        List<String> removeCardUUIdList = labelRefService.list(cardQueryWrapper)
                .stream()
                .map(LabelRef::getRefUuid)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(removeCardUUIdList)) {
            return AjaxResult.success("删除成功，没有需要从卡包移除的卡片", true);
        }
        // 删除卡包中单词集对应的卡片
        QueryWrapper<PackageCardRef> packageCardQueryWrapper = new QueryWrapper<>();
        packageCardQueryWrapper.eq("package_uuid", removeCollectionOfPackage.getPackageUUId());
        packageCardQueryWrapper.in("card_uuid", removeCardUUIdList);
        packageCardRefService.remove(packageCardQueryWrapper);
        return AjaxResult.success(true);
    }

    @Override
    public AjaxResult removeWordOfCollection(WordCollectionData wordCollectionData) {
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ref_type", RefTypeEnum.WORD.getValue());
        queryWrapper.eq("label_uuid", wordCollectionData.getLabelUUID());
        queryWrapper.in("ref_uuid", wordCollectionData.getWordUUIDList());
        queryWrapper.eq("create_user_id", wordCollectionData.getUserId());
        labelRefService.remove(queryWrapper);
        return AjaxResult.success(true);
    }
}
