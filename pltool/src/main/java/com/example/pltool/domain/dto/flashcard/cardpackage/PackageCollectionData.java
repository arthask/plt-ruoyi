package com.example.pltool.domain.dto.flashcard.cardpackage;


import lombok.Data;

@Data
public class PackageCollectionData {
    private Integer cardType;
    private String collectionUUID;
    private String packageUUID;
    private Long userId;
}
