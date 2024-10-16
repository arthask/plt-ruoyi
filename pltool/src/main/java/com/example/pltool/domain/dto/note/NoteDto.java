package com.example.pltool.domain.dto.note;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class NoteDto {
  private String uuid;
  private String title;
  /**
   * @see com.example.pltool.controller.business.constant.enums.NoteTypeEnum
   */
  private Integer type;
  private String content;
  private String refUUId;
  private String summary;
  private Long userId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createTime;
}
