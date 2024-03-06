package com.ruoyi.system.domain.vo;

import com.ruoyi.system.domain.Note;
import com.ruoyi.system.domain.Question;

import java.util.List;

public class NoteInfoVo {
    private List<Question> questionList;
    private Note note;

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
}
