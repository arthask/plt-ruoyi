package com.ruoyi.system.service;

import com.ruoyi.system.domain.UserStudyRecord;

import java.util.List;

/**
 * 用户学习记录Service接口
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
public interface IUserStudyRecordService 
{
    /**
     * 查询用户学习记录
     * 
     * @param id 用户学习记录主键
     * @return 用户学习记录
     */
    public UserStudyRecord selectUserStudyRecordById(Long id);

    /**
     * 查询用户学习记录列表
     * 
     * @param userStudyRecord 用户学习记录
     * @return 用户学习记录集合
     */
    public List<UserStudyRecord> selectUserStudyRecordList(UserStudyRecord userStudyRecord);

    /**
     * 新增用户学习记录
     * 
     * @param userStudyRecord 用户学习记录
     * @return 结果
     */
    public int insertUserStudyRecord(UserStudyRecord userStudyRecord);

    /**
     * 修改用户学习记录
     * 
     * @param userStudyRecord 用户学习记录
     * @return 结果
     */
    public int updateUserStudyRecord(UserStudyRecord userStudyRecord);

    /**
     * 批量删除用户学习记录
     * 
     * @param ids 需要删除的用户学习记录主键集合
     * @return 结果
     */
    public int deleteUserStudyRecordByIds(Long[] ids);

    /**
     * 删除用户学习记录信息
     * 
     * @param id 用户学习记录主键
     * @return 结果
     */
    public int deleteUserStudyRecordById(Long id);

    /**
     * 获取某个日期学习过的单词
     * @param userId 用户id
     * @param day 日期
     * @return 结果
     */
    List<UserStudyRecord> listWordOfDay(Long userId, int day);
}
