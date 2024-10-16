package com.ruoyi.common.enums;

public enum SnapshotType {
  REVIEW("REVIEW", 0), NEW("NEW", 1);

  private final String code;
  private final Integer value;

  SnapshotType(String code, Integer value) {
    this.code = code;
    this.value = value;
  }

  public String getCode() {
    return code;
  }

  public Integer getValue() {
    return value;
  }
}
