package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.enums.PeriodEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserWord;
import com.ruoyi.system.mapper.UserWordMapper;
import com.ruoyi.system.service.IUserWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用户单词Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service
public class UserWordServiceImpl implements IUserWordService {
    @Autowired
    private UserWordMapper userWordMapper;

    /**
     * 查询用户单词
     *
     * @param id 用户单词主键
     * @return 用户单词
     */
    @Override
    public UserWord selectUserWordById(Long id) {
        return userWordMapper.selectUserWordById(id);
    }

    /**
     * 查询用户单词列表
     *
     * @param userWord 用户单词
     * @return 用户单词
     */
    @Override
    public List<UserWord> selectUserWordList(UserWord userWord) {
        return userWordMapper.selectUserWordList(userWord);
    }

    /**
     * 新增用户单词
     *
     * @param userWord 用户单词
     * @return 结果
     */
    @Override
    public int insertUserWord(UserWord userWord) {
        // 检查一下是否有重复的
        UserWord repeatWord = userWordMapper.findByWordWord(userWord.getWord());
        if (Objects.nonNull(repeatWord)) {
            setPeriodAndNextTime(repeatWord, repeatWord.getPeriod() + 1L);
            return userWordMapper.updateUserWord(repeatWord);
        }
        setPeriodAndNextTime(userWord, 1L);
        userWord.setCreateTime(DateUtils.getNowDate());
        return userWordMapper.insertUserWord(userWord);
    }


    /**
     * 修改用户单词
     *
     * @param userWord 用户单词
     * @return 结果
     */
    @Override
    public int updateUserWord(UserWord userWord) {
        userWord.setUpdateTime(DateUtils.getNowDate());
        return userWordMapper.updateUserWord(userWord);
    }

    /**
     * 批量删除用户单词
     *
     * @param ids 需要删除的用户单词主键
     * @return 结果
     */
    @Override
    public int deleteUserWordByIds(Long[] ids) {
        return userWordMapper.deleteUserWordByIds(ids);
    }

    /**
     * 删除用户单词信息
     *
     * @param id 用户单词主键
     * @return 结果
     */
    @Override
    public int deleteUserWordById(Long id) {
        return userWordMapper.deleteUserWordById(id);
    }

    /**
     * 获取下次学习时间
     *
     * @param period 阶段
     * @return 下次学习时间
     */
    private Date getNextStudyTime(Long period) {
        Long minutes = PeriodEnum.getByPeriod(period);
        if (Objects.isNull(minutes)) {
            return null;
        }
        long millis = TimeUnit.MINUTES.toMillis(minutes);
        return new Date(DateUtil.current() + millis);
    }

    /**
     * 设置阶段和下次学习时间
     * @param userWord 单词
     * @param period 阶段
     */
    private void setPeriodAndNextTime(UserWord userWord, Long period) {
        userWord.setPeriod(period);
        Date nextStudyTime = getNextStudyTime(userWord.getPeriod());
        boolean notUpdateFlag = Objects.isNull(nextStudyTime);
        if (notUpdateFlag) {
            return;
        }
        userWord.setNextStudyTime(nextStudyTime);
    }
}
