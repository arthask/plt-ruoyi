package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserStudyRecord;
import com.ruoyi.system.mapper.OldUserStudyRecordMapper;
import com.ruoyi.system.service.IUserStudyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 用户学习记录Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-10
 */
@Service("oldUserStudyRecordService")
public class UserStudyRecordServiceImpl implements IUserStudyRecordService {
    @Autowired
    private OldUserStudyRecordMapper oldUserStudyRecordMapper;

    /**
     * 查询用户学习记录
     *
     * @param id 用户学习记录主键
     * @return 用户学习记录
     */
    @Override
    public UserStudyRecord selectUserStudyRecordById(Long id) {
        return oldUserStudyRecordMapper.selectUserStudyRecordById(id);
    }

    /**
     * 查询用户学习记录列表
     *
     * @param userStudyRecord 用户学习记录
     * @return 用户学习记录
     */
    @Override
    public List<UserStudyRecord> selectUserStudyRecordList(UserStudyRecord userStudyRecord) {
        return oldUserStudyRecordMapper.selectUserStudyRecordList(userStudyRecord);
    }

    /**
     * 新增用户学习记录
     *
     * @param userStudyRecord 用户学习记录
     * @return 结果
     */
    @Override
    public int insertUserStudyRecord(UserStudyRecord userStudyRecord) {
        userStudyRecord.setCreateTime(DateUtils.getNowDate());
        return oldUserStudyRecordMapper.insertUserStudyRecord(userStudyRecord);
    }

    /**
     * 修改用户学习记录
     *
     * @param userStudyRecord 用户学习记录
     * @return 结果
     */
    @Override
    public int updateUserStudyRecord(UserStudyRecord userStudyRecord) {
        userStudyRecord.setUpdateTime(DateUtils.getNowDate());
        return oldUserStudyRecordMapper.updateUserStudyRecord(userStudyRecord);
    }

    /**
     * 批量删除用户学习记录
     *
     * @param ids 需要删除的用户学习记录主键
     * @return 结果
     */
    @Override
    public int deleteUserStudyRecordByIds(Long[] ids) {
        return oldUserStudyRecordMapper.deleteUserStudyRecordByIds(ids);
    }

    /**
     * 删除用户学习记录信息
     *
     * @param id 用户学习记录主键
     * @return 结果
     */
    @Override
    public int deleteUserStudyRecordById(Long id) {
        return oldUserStudyRecordMapper.deleteUserStudyRecordById(id);
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
            Date date = DateUtils.parseDate(year + "-" + monthStr + "-" + day + " " + "00:00:00", DateUtils.YYYY_MM_DD_HH_MM_SS);
            DateTime begin = DateUtil.beginOfDay(date);
            DateTime end = DateUtil.endOfDay(date);
            return oldUserStudyRecordMapper.listWordOfDay(userId, DateUtil.format(begin, DateUtils.YYYY_MM_DD_HH_MM_SS),
                    DateUtil.format(end, DateUtils.YYYY_MM_DD_HH_MM_SS));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
