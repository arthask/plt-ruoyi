package com.example.pltool.domain.dto.flashcard.cardpackage;

import java.util.List;

import lombok.Data;

@Data
public class OperateWordInCollection extends PackageCollectionData {
  private List<String> packageUUIdList;
}
