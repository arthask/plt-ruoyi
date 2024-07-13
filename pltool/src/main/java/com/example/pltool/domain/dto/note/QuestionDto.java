package com.example.pltool.domain.dto.note;




import com.example.pltool.domain.entity.Question;

import java.util.List;

public class QuestionDto {
    private String noteTitle;
    private List<Question> questions;

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
