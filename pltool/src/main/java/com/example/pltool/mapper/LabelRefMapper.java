package com.example.pltool.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.dto.language.wordcollection.WordCollectionData;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.domain.entity.Word;

/**
 * <p>
 * 标签关联表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Mapper
public interface LabelRefMapper extends BaseMapper<LabelRef> {
  List<LabelInfo> getLabelInfoByRefUUID(@Param("refUUID") String refUUID);

  List<WordCollectionData> getAllCollectionByType(@Param("type") Integer type);

  List<WordCollectionData> getCollectionsOfPackage(@Param("packageUUId") String packageUUId,
      @Param("userId") Long userId);

  List<Word> getWordsOfCollection(@Param("uuid") String uuid);

  List<Word> getWordsOfCollectionUUIdList(@Param("labelUUIDList") List<String> labelUUIDList);

  List<WordCollectionData> getCollectionOfWord(@Param("wordUUId") String wordUUId,
      @Param("userId") Long userId);
}
