package com.example.pltool.service.impl.language;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.pltool.controller.business.constant.enums.RefTypeEnum;
import com.example.pltool.domain.dto.flashcard.cardpackage.OperateWordInCollection;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageCollectionData;
import com.example.pltool.domain.dto.flashcard.cardpackage.RemoveCollectionOfPackage;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.domain.entity.PackageCardRef;
import com.example.pltool.domain.entity.Word;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.LabelService;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.flashcard.CreateCardService;
import com.example.pltool.service.flashcard.FlashcardService;
import com.example.pltool.service.language.WordCollectionService;
import com.example.pltool.service.language.WordService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.uuid.UUID;

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

  @Resource(name = "wordCardCreator")
  private CreateCardService<Word> wordCardCreator;

  @Autowired
  private WordService wordService;

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
    return labelRefService.getCollectionsOfPackage(packageUUId, userId);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult addWordToCollection(WordCollectionData wordCollectionData) {
    // 相同单词添加到相同单词集 需要过滤
    QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("ref_type", RefTypeEnum.WORD.getValue());
    queryWrapper.eq("create_user_id", wordCollectionData.getUserId());
    queryWrapper.eq("label_uuid", wordCollectionData.getLabelUUID());
    queryWrapper.in("ref_uuid", wordCollectionData.getWordUUIDList());
    List<LabelRef> labelRefs = labelRefService.list(queryWrapper);
    Map<String, List<LabelRef>> labelWordListMap =
        labelRefs.stream().collect(Collectors.groupingBy(LabelRef::getLabelUuid));

    List<String> filterdWordUUIdList = new ArrayList<>();
    boolean filteredFlag = false;
    if (labelWordListMap.containsKey(wordCollectionData.getLabelUUID())) {
      // 获取单词的uuid
      List<String> existWordUUIdList = labelWordListMap.get(wordCollectionData.getLabelUUID())
          .stream().map(LabelRef::getRefUuid).collect(Collectors.toList());
      filterdWordUUIdList.addAll(wordCollectionData.getWordUUIDList().stream()
          .filter(e -> !existWordUUIdList.contains(e)).collect(Collectors.toList()));
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
    // 如果这个单词集有绑定卡包，则需要生成卡片并绑定卡片对应关系
    QueryWrapper<PackageCardRef> packageCardRefQueryWrapper = new QueryWrapper<>();
    packageCardRefQueryWrapper.eq("collection_uuid", wordCollectionData.getLabelUUID());
    packageCardRefQueryWrapper.eq("create_user_id", wordCollectionData.getUserId());
    List<PackageCardRef> packageCardRefs = packageCardRefService.list(packageCardRefQueryWrapper);
    if (!CollectionUtils.isEmpty(packageCardRefs)) {
      Map<String, List<PackageCardRef>> packageMap =
          packageCardRefs.stream().collect(Collectors.groupingBy(PackageCardRef::getPackageUuid));
      List<Word> wordsOfCollection = wordService.getWordListByUUID(filterdWordUUIdList);
      OperateWordInCollection operateWordInCollection = new OperateWordInCollection();
      operateWordInCollection.setCollectionUUID(wordCollectionData.getLabelUUID());
      operateWordInCollection.setUserId(wordCollectionData.getUserId());
      operateWordInCollection.setPackageUUIdList(new ArrayList<>(packageMap.keySet()));
      wordCardCreator.createCardAndRelationShip(operateWordInCollection, wordsOfCollection);
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
    boolean repeatBindWordCollection =
        labelRefService.isRepeatBindWordCollection(packageCollectionData.getPackageUUID(),
            packageCollectionData.getCollectionUUID(), packageCollectionData.getUserId());
    if (repeatBindWordCollection) {
      return AjaxResult.success("该单词集已绑定，请重新选择", true);
    }
    return flashcardService.batchAddCardByWordCollection(packageCollectionData);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult removeCollectionOfPackage(RemoveCollectionOfPackage removeCollectionOfPackage) {
    // 删除卡包与单词集的关联关系
    QueryWrapper<PackageCardRef> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("package_uuid", removeCollectionOfPackage.getPackageUUId());
    queryWrapper.in("collection_uuid", removeCollectionOfPackage.getCollectionUUIdList());
    queryWrapper.eq("create_user_id", removeCollectionOfPackage.getUserId());
    if (packageCardRefService.count(queryWrapper) > 0) {
      packageCardRefService.remove(queryWrapper);
      return AjaxResult.success(true);
    }
    return AjaxResult.success("删除成功，单词集已从卡包移除", true);
  }

  @Override
  public AjaxResult removeWordOfCollection(WordCollectionData wordCollectionData) {
    // 删除卡片
    return wordCardCreator.delCardRelationShip(wordCollectionData.getWordUUIDList(),
        wordCollectionData.getLabelUUID(), wordCollectionData.getUserId());

  }

  @Override
  public List<WordCollectionData> getCollectionOfWord(String wordUUId, Long userId) {
    return labelRefService.getCollectionOfWord(wordUUId, userId);
  }
}
