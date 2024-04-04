package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 标签关联对象 label_ref
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public class LabelRef extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** uuid */
    @Excel(name = "uuid")
    private String uuid;

    /** 关联uuid 单词uuid或者词库uuid */
    @Excel(name = "关联uuid 单词uuid或者词库uuid")
    private Long refUuid;

    /** 关联类型 0：代表词库 1：代表单词 */
    @Excel(name = "关联类型 0：代表词库 1：代表单词")
    private Integer refType;

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
    public void setRefUuid(Long refUuid) 
    {
        this.refUuid = refUuid;
    }

    public Long getRefUuid() 
    {
        return refUuid;
    }
    public void setRefType(Integer refType) 
    {
        this.refType = refType;
    }

    public Integer getRefType() 
    {
        return refType;
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
            .append("refUuid", getRefUuid())
            .append("refType", getRefType())
            .append("createUserId", getCreateUserId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
