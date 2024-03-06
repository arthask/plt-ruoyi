package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 对话对象 dialogue
 * 
 * @author ruoyi
 * @date 2024-02-25
 */
public class Dialogue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** uuid */
    @Excel(name = "uuid")
    private String uuid;

    /** 场景id */
    @Excel(name = "场景id")
    private Long sceneId;

    /** 发送内容 */
    @Excel(name = "发送内容")
    private String senderContent;

    /** 回复 */
    @Excel(name = "回复")
    private String reply;

    /** 在对话中的排序号 */
    @Excel(name = "在对话中的排序号")
    private Long sortNum;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createUserId;

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
    public void setSceneId(Long sceneId) 
    {
        this.sceneId = sceneId;
    }

    public Long getSceneId() 
    {
        return sceneId;
    }
    public void setSenderContent(String senderContent) 
    {
        this.senderContent = senderContent;
    }

    public String getSenderContent() 
    {
        return senderContent;
    }
    public void setReply(String reply) 
    {
        this.reply = reply;
    }

    public String getReply() 
    {
        return reply;
    }
    public void setSortNum(Long sortNum) 
    {
        this.sortNum = sortNum;
    }

    public Long getSortNum() 
    {
        return sortNum;
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
            .append("uuid", getUuid())
            .append("sceneId", getSceneId())
            .append("senderContent", getSenderContent())
            .append("reply", getReply())
            .append("sortNum", getSortNum())
            .append("createUserId", getCreateUserId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
