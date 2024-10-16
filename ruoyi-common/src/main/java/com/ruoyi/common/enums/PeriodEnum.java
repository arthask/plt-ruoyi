package com.ruoyi.common.enums;

public enum PeriodEnum {
  /**
   * 5分钟
   */
  LEVEL_ONE(1, 5L),
  /**
   * 30分钟
   */
  LEVEL_TWO(2, 30L),
  /**
   * 12小时
   */
  LEVEL_THREE(3, 720L),
  /**
   * 24小时
   */
  LEVEL_FOUR(4, 1440L),
  /**
   * 2天 48小时
   */
  LEVEL_FIVE(5, 2880L),
  /**
   * 4天 96小时
   */
  LEVEL_SIX(6, 5760L),
  /**
   * 7天 168小时
   */
  LEVEL_SEVEN(7, 10080L),
  /**
   * 15天 360小时
   */
  LEVEL_EIGHT(8, 92160L);

  private final Long value;
  private final Integer code;

  PeriodEnum(Integer code, Long value) {
    this.code = code;
    this.value = value;
  }

  public static Long getByPeriod(Integer code) {
    for (PeriodEnum periodEnum : PeriodEnum.values()) {
      if (periodEnum.getCode().equals(code)) {
        return periodEnum.getValue();
      }
    }
    return null;
  }

  public Integer getCode() {
    return code;
  }

  public Long getValue() {
    return value;
  }
}
