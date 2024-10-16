package com.example.pltool.domain.dto.language.wordsentence;

import java.util.List;

import lombok.Data;

@Data
public class WordSentenceDto {
  private String wordUUId;
  private List<SentenceDto> sentenceDtoList;
  private List<String> removeSentenceUUIdList;
}
