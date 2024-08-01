package com.example.pltool.domain.vo;

public class StatisticsCountVo {
    /**
     * 词库数量
     */
    private Long wordDataBaseNum;
    /**
     * 词库总单词数
     */
    private Long wordCount;
    /**
     * 我的单词数
     */
    private Long myWordCount;
    /**
     * 学习记录数
     */
    private Long studyRecordCount;

    public Long getWordDataBaseNum() {
        return wordDataBaseNum;
    }

    public void setWordDataBaseNum(Long wordDataBaseNum) {
        this.wordDataBaseNum = wordDataBaseNum;
    }

    public Long getWordCount() {
        return wordCount;
    }

    public void setWordCount(Long wordCount) {
        this.wordCount = wordCount;
    }

    public Long getMyWordCount() {
        return myWordCount;
    }

    public void setMyWordCount(Long myWordCount) {
        this.myWordCount = myWordCount;
    }

    public Long getStudyRecordCount() {
        return studyRecordCount;
    }

    public void setStudyRecordCount(Long studyRecordCount) {
        this.studyRecordCount = studyRecordCount;
    }
}
