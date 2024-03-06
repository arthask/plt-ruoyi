package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 用户单词对象 user_word
 * 
 * @author ruoyi
 * @date 2024-01-15
 */
public class UserWord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 单词id */
    @Excel(name = "单词id")
    private Long wordId;

    /** 单词 */
    @Excel(name = "单词")
    private String word;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 用户名称 */
    @Excel(name = "用户名称")
    private String userName;

    /** 阶段，拥有标识单词的单词的熟悉程度 */
    @Excel(name = "阶段，拥有标识单词的单词的熟悉程度")
    private Long period;

    /** 是否收藏 */
    @Excel(name = "是否收藏")
    private String collectFlag;

    /** 下次学习时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "下次学习时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date nextStudyTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWordId(Long wordId) 
    {
        this.wordId = wordId;
    }

    public Long getWordId() 
    {
        return wordId;
    }
    public void setWord(String word) 
    {
        this.word = word;
    }

    public String getWord() 
    {
        return word;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }
    public void setPeriod(Long period) 
    {
        this.period = period;
    }

    public Long getPeriod() 
    {
        return period;
    }
    public void setCollectFlag(String collectFlag) 
    {
        this.collectFlag = collectFlag;
    }

    public String getCollectFlag() 
    {
        return collectFlag;
    }
    public void setNextStudyTime(Date nextStudyTime) 
    {
        this.nextStudyTime = nextStudyTime;
    }

    public Date getNextStudyTime() 
    {
        return nextStudyTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("wordId", getWordId())
            .append("word", getWord())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("period", getPeriod())
            .append("collectFlag", getCollectFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("nextStudyTime", getNextStudyTime())
            .toString();
    }
}
