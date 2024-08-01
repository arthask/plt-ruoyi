package com.example.pltool.domain.dto.language.word;




import com.example.pltool.domain.entity.Word;

import java.util.List;

public class WordShowData extends Word {
    private List<String> lexiconName;
    private List<String> labelList;

    public List<String> getLexiconName() {
        return lexiconName;
    }

    public void setLexiconName(List<String> lexiconName) {
        this.lexiconName = lexiconName;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }
}
