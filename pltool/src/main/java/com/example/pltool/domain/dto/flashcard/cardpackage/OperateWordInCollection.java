package com.example.pltool.domain.dto.flashcard.cardpackage;

import lombok.Data;

import java.util.List;

@Data
public class OperateWordInCollection extends PackageCollectionData{
    private List<String> packageUUIdList;
}
