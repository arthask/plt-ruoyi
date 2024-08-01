package com.example.pltool.domain.dto.flashcard.card;


import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.Flashcard;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardInfo extends Flashcard {
    private PackageInfoDto packageInfoDto;
}
