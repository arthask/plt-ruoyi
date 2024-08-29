package com.example.pltool.domain.dto.language.wordsentence;

import lombok.Data;

import java.util.List;

@Data
public class WordSentenceDto {
    private String wordUUId;
    private List<SentenceDto> sentenceDtoList;
    private List<String> removeSentenceUUIdList;
}
