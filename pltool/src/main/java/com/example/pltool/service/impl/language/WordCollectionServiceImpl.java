package com.example.pltool.service.impl.language;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pltool.controller.business.constant.enums.RefTypeEnum;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.LabelService;
import com.example.pltool.service.language.WordCollectionService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WordCollectionServiceImpl implements WordCollectionService {
    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private LabelService labelService;

    @Override
    public boolean addWordCollectionData(WordCollectionData wordCollectionData) {
        return false;
    }

    @Override
    public AjaxResult updateWordCollectionData(WordCollectionData wordCollectionData) {
        // 先检查是否重名
        if(labelService.isDuplicateName(wordCollectionData.getName())) {
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
    public AjaxResult addWordToCollection(WordCollectionData wordCollectionData) {
        // 对单词和标签进行绑定
        List<LabelRef> addLabelRefList = new ArrayList<>();
        wordCollectionData.getWordUUIDList().forEach(e -> {
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
        return AjaxResult.success(true);
    }
}
