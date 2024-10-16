package com.example.pltool.domain.dto.language.lexicon;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.pltool.domain.entity.Label;
import com.example.pltool.domain.entity.Lexicon;

public class LexiconShowData extends Lexicon {
  private List<Label> deleteTags;
  private List<Label> labelList;
  private MultipartFile file;

  public List<Label> getLabelList() {
    return labelList;
  }

  public void setLabelList(List<Label> labelList) {
    this.labelList = labelList;
  }

  public MultipartFile getFile() {
    return file;
  }

  public void setFile(MultipartFile file) {
    this.file = file;
  }

  public List<Label> getDeleteTags() {
    return deleteTags;
  }

  public void setDeleteTags(List<Label> deleteTags) {
    this.deleteTags = deleteTags;
  }
}
