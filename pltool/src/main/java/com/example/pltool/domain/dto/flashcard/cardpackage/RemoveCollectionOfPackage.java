package com.example.pltool.domain.dto.flashcard.cardpackage;

import java.util.List;

import lombok.Data;

@Data
public class RemoveCollectionOfPackage {
  private Long userId;
  private String packageUUId;
  private List<String> collectionUUIdList;
}
