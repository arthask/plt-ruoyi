package com.example.filespringbootstarter.utils.date;


import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtils {
  public static final String datePath() {
    Date now = new Date();
    return DateFormatUtils.format(now, "yyyy/MM/dd");
  }
}
