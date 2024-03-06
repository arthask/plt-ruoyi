package com.ruoyi.common.enums;

public enum PeriodEnum {
    /**
     * 5分钟
     */
    LEVEL_ONE(1L, 5L),
    /**
     * 30分钟
     */
    LEVEL_TWO(2L, 30L),
    /**
     * 12小时
     */
    LEVEL_THREE(3L, 720L),
    /**
     * 24小时
     */
    LEVEL_FOUR(4L, 1440L),
    /**
     * 2天 48小时
     */
    LEVEL_FIVE(5L, 2880L),
    /**
     * 4天 96小时
     */
    LEVEL_SIX(6L, 5760L),
    /**
     * 7天 168小时
     */
    LEVEL_SEVEN(7L, 10080L),
    /**
     * 15天 360小时
     */
    LEVEL_EIGHT(8L, 92160L);

    private final Long value;
    private final Long code;

    PeriodEnum(Long code,Long value) {
        this.value = value;
        this.code = code;
    }

    public Long getCode() {
        return code;
    }

    public Long getValue() {
        return value;
    }

    public static Long getByPeriod(Long code) {
        for (PeriodEnum periodEnum : PeriodEnum.values()) {
            if (periodEnum.getCode().equals(code)) {
                return periodEnum.getValue();
            }
        }
        return null;
    }
}
