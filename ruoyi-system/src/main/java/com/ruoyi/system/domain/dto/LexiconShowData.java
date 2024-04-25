package com.ruoyi.system.domain.dto;

import com.ruoyi.system.gencode.entity.Label;
import com.ruoyi.system.gencode.entity.Lexicon;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
