package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 词库与单词关系对象 lexicon_word
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public class LexiconWord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** uuid */
    @Excel(name = "uuid")
    private String uuid;

    /** 词库id */
    @Excel(name = "词库id")
    private Long lexionId;

    /** 单词id */
    @Excel(name = "单词id")
    private Long wordId;

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
    public void setLexionId(Long lexionId) 
    {
        this.lexionId = lexionId;
    }

    public Long getLexionId() 
    {
        return lexionId;
    }
    public void setWordId(Long wordId) 
    {
        this.wordId = wordId;
    }

    public Long getWordId() 
    {
        return wordId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uuid", getUuid())
            .append("lexionId", getLexionId())
            .append("wordId", getWordId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
