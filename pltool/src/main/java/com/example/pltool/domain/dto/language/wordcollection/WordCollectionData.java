package com.example.pltool.domain.dto.language.wordcollection;

import com.example.pltool.domain.entity.Word;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WordCollectionData {
    /**
     * 标签uuid
     */
    private String labelUUID;
    /**
     * 标签名
     */
    private String name;

    private Long userId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private List<Word> wordList;

    /**
     * 单词uuid列表
     */
    private List<String> wordUUIDList;
}
