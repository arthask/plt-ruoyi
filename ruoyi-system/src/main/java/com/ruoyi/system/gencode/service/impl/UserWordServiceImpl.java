package com.ruoyi.system.gencode.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.enums.PeriodEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.gencode.entity.UserWord;
import com.ruoyi.system.gencode.entity.Word;
import com.ruoyi.system.gencode.mapper.UserWordMapper;
import com.ruoyi.system.gencode.service.UserWordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.gencode.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户单词表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Service("userWordService")
public class UserWordServiceImpl extends ServiceImpl<UserWordMapper, UserWord> implements UserWordService {
    @Autowired
    private UserWordMapper userWordMapper;

    @Autowired
    private WordService wordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int collect(String wordUUID, Long userId, String userName) {
        QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
        wordQueryWrapper.eq("uuid", wordUUID);
        Word word = wordService.getOne(wordQueryWrapper);
        Assert.notNull(word, "请求参数错误");
        // 检查一下是否有重复的
        QueryWrapper<UserWord> userWordQueryWrapper = new QueryWrapper<>();
        userWordQueryWrapper.eq("word_id", word.getId());
        UserWord repeatWord = userWordMapper.selectOne(userWordQueryWrapper);
        if (Objects.nonNull(repeatWord)) {
            repeatWord.setCollectFlag(1);
            return userWordMapper.updateById(repeatWord);
        }
        UserWord userWord = new UserWord()
                .setCollectFlag(1)
                .setWord(word.getWord())
                .setWordId(word.getId())
                .setUserId(userId)
                .setUserName(userName);
        setPeriodAndNextTime(userWord, 1);
        return userWordMapper.insert(userWord);
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
        QueryWrapper<UserWord> userWordQueryWrapper = new QueryWrapper<>();
        userWordQueryWrapper.eq("word_id", userWord.getId());
        UserWord repeatWord = userWordMapper.selectOne(userWordQueryWrapper);
        if (Objects.nonNull(repeatWord)) {
            setPeriodAndNextTime(repeatWord, repeatWord.getPeriod() + 1);
            return userWordMapper.updateById(repeatWord);
        }
        setPeriodAndNextTime(userWord, 1);
        userWord.setCollectFlag(0);
        return userWordMapper.insert(userWord);
    }

    /**
     * 获取下次学习时间
     *
     * @param period 阶段
     * @return 下次学习时间
     */
    private Date getNextStudyTime(Integer period) {
        Long minutes = PeriodEnum.getByPeriod(period);
        if (Objects.isNull(minutes)) {
            return null;
        }
        long millis = TimeUnit.MINUTES.toMillis(minutes);
        return new Date(DateUtil.current() + millis);
    }

    /**
     * 设置阶段和下次学习时间
     *
     * @param userWord 单词
     * @param period   阶段
     */
    private void setPeriodAndNextTime(UserWord userWord, Integer period) {
        userWord.setPeriod(period);
        Date nextStudyTime = getNextStudyTime(userWord.getPeriod());
        boolean notUpdateFlag = Objects.isNull(nextStudyTime);
        if (notUpdateFlag) {
            return;
        }
        userWord.setNextStudyTime(LocalDateTime.ofInstant(nextStudyTime.toInstant(), ZoneId.systemDefault()));
    }
}
