package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 单词例句对象 word_sentence
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public class WordSentence extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** uuid */
    @Excel(name = "uuid")
    private String uuid;

    /** 单词id */
    @Excel(name = "单词id")
    private Long wordId;

    /** 例句内容 */
    @Excel(name = "例句内容")
    private String sentenceContent;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUuid(String uuid) 
    {
        this.uuid = uuid;
    }

    public String getUuid() 
    {
        return uuid;
    }
    public void setWordId(Long wordId) 
    {
        this.wordId = wordId;
    }

    public Long getWordId() 
    {
        return wordId;
    }
    public void setSentenceContent(String sentenceContent) 
    {
        this.sentenceContent = sentenceContent;
    }

    public String getSentenceContent() 
    {
        return sentenceContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uuid", getUuid())
            .append("wordId", getWordId())
            .append("sentenceContent", getSentenceContent())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
