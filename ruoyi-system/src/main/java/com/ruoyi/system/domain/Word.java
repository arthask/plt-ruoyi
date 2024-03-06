package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 单词对象 word
 * 
 * @author ruoyi
 * @date 2024-01-18
 */
public class Word extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 单词 */
    @Excel(name = "单词")
    private String word;

    /** 音标 */
    @Excel(name = "音标")
    private String phonetic;

    /** 翻译 */
    @Excel(name = "翻译")
    private String translation;

    /** 词性 */
    @Excel(name = "词性")
    private String pos;

    /** 标签 */
    @Excel(name = "标签")
    private String tag;

    /** 例句 */
    @Excel(name = "例句")
    private String sentence;

    private Long createUserId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWord(String word) 
    {
        this.word = word;
    }

    public String getWord() 
    {
        return word;
    }
    public void setPhonetic(String phonetic) 
    {
        this.phonetic = phonetic;
    }

    public String getPhonetic() 
    {
        return phonetic;
    }
    public void setTranslation(String translation) 
    {
        this.translation = translation;
    }

    public String getTranslation() 
    {
        return translation;
    }
    public void setPos(String pos) 
    {
        this.pos = pos;
    }

    public String getPos() 
    {
        return pos;
    }
    public void setTag(String tag) 
    {
        this.tag = tag;
    }

    public String getTag() 
    {
        return tag;
    }
    public void setSentence(String sentence) 
    {
        this.sentence = sentence;
    }

    public String getSentence() 
    {
        return sentence;
    }
    public void setCreateUserId(Long createUserId) 
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId() 
    {
        return createUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("word", getWord())
            .append("phonetic", getPhonetic())
            .append("translation", getTranslation())
            .append("pos", getPos())
            .append("tag", getTag())
            .append("sentence", getSentence())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createUserId", getCreateUserId())
            .toString();
    }
}
