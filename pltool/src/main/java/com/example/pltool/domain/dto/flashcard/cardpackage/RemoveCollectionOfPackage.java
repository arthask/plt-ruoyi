package com.example.pltool.domain.dto.flashcard.cardpackage;

import lombok.Data;

import java.util.List;

@Data
public class RemoveCollectionOfPackage {
    private Long userId;
    private String packageUUId;
    private List<String> collectionUUIdList;
}
