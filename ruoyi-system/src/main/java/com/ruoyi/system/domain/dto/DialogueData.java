package com.ruoyi.system.domain.dto;

public class DialogueData {
    /**
     * 发送内容
     */
    private String senderContent;

    /**
     * 回复
     */
    private String reply;

    /**
     * 在对话中的排序号
     */
    private Long sortNum;

    public String getSenderContent() {
        return senderContent;
    }

    public void setSenderContent(String senderContent) {
        this.senderContent = senderContent;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Long getSortNum() {
        return sortNum;
    }

    public void setSortNum(Long sortNum) {
        this.sortNum = sortNum;
    }
}
