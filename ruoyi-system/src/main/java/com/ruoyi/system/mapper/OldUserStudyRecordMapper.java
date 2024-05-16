package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserStudyRecord;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户学习记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-01-10
 */
public interface OldUserStudyRecordMapper
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
     * 删除用户学习记录
     * 
     * @param id 用户学习记录主键
     * @return 结果
     */
    public int deleteUserStudyRecordById(Long id);

    /**
     * 批量删除用户学习记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserStudyRecordByIds(Long[] ids);

    /**
     * 获取学习记录数量
     * @return 学习记录数量
     */
    Long getStudyRecordCount(@Param("userId") Long userId);

    /**
     * 每月学习单词数统计
     * @param userId 用户id
     * @param beginStr 年开始时间
     * @param endStr 年结束时间
     * @return 每月学习单词数统计
     */
    List<Long> getStudyRecordOfMonth(@Param("userId") Long userId, @Param("beginStr") String beginStr, @Param("endStr")String endStr);

    /**
     * 每天实际值
     * @param beginStr 月开始时间
     * @param endStr 月结束时间
     * @return 每天期望值
     */
    @MapKey("day")
    Map<String, Map<String, Long>> getActualValueOfDay(@Param("userId") Long userId, @Param("beginStr") String beginStr, @Param("endStr")String endStr);

    /**
     * 当天的学习过的单词
     * @param userId 用户id
     * @param beginStr 开始时间
     * @param endStr 结束时间
     * @return 学习记录
     */
    List<UserStudyRecord> listWordOfDay(@Param("userId") Long userId,
                                        @Param("beginStr") String beginStr,
                                        @Param("endStr")String endStr);
}
