package com.example.pltool.domain.dto.language.lexicon;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class LexiconData {

  private MultipartFile file;
  private Long userId;
  private String name;
  private String language;
  private List<String> labelList;

  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public List<String> getLabelList() {
    return labelList;
  }

  public void setLabelList(List<String> labelList) {
    this.labelList = labelList;
  }
}
