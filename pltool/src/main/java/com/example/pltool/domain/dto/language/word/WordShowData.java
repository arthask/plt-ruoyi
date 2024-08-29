package com.example.pltool.domain.dto.language.word;


import com.example.pltool.domain.entity.Word;
import com.example.pltool.domain.entity.WordSentence;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WordShowData extends Word {
    private List<WordSentence> sentenceList;
}
