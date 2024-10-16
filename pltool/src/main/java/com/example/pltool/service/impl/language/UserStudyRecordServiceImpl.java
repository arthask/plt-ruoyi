package com.example.pltool.service.impl.language;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.entity.UserStudyRecord;
import com.example.pltool.mapper.UserStudyRecordMapper;
import com.example.pltool.service.language.UserStudyRecordService;
import com.ruoyi.common.utils.DateUtils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;

/**
 * <p>
 * 用户学习记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Service
public class UserStudyRecordServiceImpl extends ServiceImpl<UserStudyRecordMapper, UserStudyRecord>
    implements UserStudyRecordService {
  @Override
  public int insertUserStudyRecord(UserStudyRecord userStudyRecord) {
    userStudyRecord.setUuid(UUID.randomUUID().toString().replace("-", ""));
    userStudyRecord
        .setStudyTime(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
    userStudyRecord.setCreateTime(
        LocalDateTime.ofInstant(DateUtils.getNowDate().toInstant(), ZoneId.systemDefault()));
    return getBaseMapper().insert(userStudyRecord);
  }

  @Override
  public List<UserStudyRecord> listWordOfDay(Long userId, int day) {
    try {
      // 获取当天开始和结束时间
      int year = DateUtil.thisYear();
      int month = DateUtil.thisMonth() + 1;
      String monthStr = String.valueOf(month);
      if (monthStr.length() < 2) {
        monthStr = "0" + monthStr;
      }
      Date date = DateUtils.parseDate(year + "-" + monthStr + "-" + day + " " + "00:00:00",
          DateUtils.YYYY_MM_DD_HH_MM_SS);
      DateTime begin = DateUtil.beginOfDay(date);
      DateTime end = DateUtil.endOfDay(date);
      return getBaseMapper().listWordOfDay(userId,
          DateUtil.format(begin, DateUtils.YYYY_MM_DD_HH_MM_SS),
          DateUtil.format(end, DateUtils.YYYY_MM_DD_HH_MM_SS));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<UserStudyRecord> getStudyRecordsOfWord(String wordUUId) {
    QueryWrapper<UserStudyRecord> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("word_uuid", wordUUId);
    return list(queryWrapper);
  }
}
