package com.example.pltool.service.flashcard;

import java.util.List;

import com.example.pltool.domain.dto.flashcard.cardpackage.OperateWordInCollection;
import com.ruoyi.common.core.domain.AjaxResult;

public interface CreateCardService<T> {
  /**
   * 创建卡片
   * 
   * @param operateWordInCollection
   * @param sourceItem
   * @return
   */
  AjaxResult createCardAndRelationShip(OperateWordInCollection operateWordInCollection,
      List<T> sourceItem);

  /**
   * 删除卡片关联数据
   * 
   * @param UUIdList
   * @param collectionUUId
   * @param userId
   * @return
   */
  AjaxResult delCardRelationShip(List<String> UUIdList, String collectionUUId, Long userId);
}
