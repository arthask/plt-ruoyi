package com.example.pltool.domain.dto.scene;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DialogueData {
  @JsonIgnore
  private Long id;
  /**
   * uuid
   */
  private String uuid;
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
  private Integer sortNum;

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

  public Integer getSortNum() {
    return sortNum;
  }

  public void setSortNum(Integer sortNum) {
    this.sortNum = sortNum;
  }

  public String getUuid() {
    return uuid;
  }

  public DialogueData setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

  public Long getId() {
    return id;
  }

  public DialogueData setId(Long id) {
    this.id = id;
    return this;
  }
}
