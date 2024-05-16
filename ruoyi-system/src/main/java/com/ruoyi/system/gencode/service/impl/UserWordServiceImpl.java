package com.ruoyi.system.gencode.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.enums.PeriodEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.dto.WordShowData;
import com.ruoyi.system.gencode.entity.Label;
import com.ruoyi.system.gencode.entity.LexiconWord;
import com.ruoyi.system.gencode.entity.UserWord;
import com.ruoyi.system.gencode.entity.Word;
import com.ruoyi.system.gencode.mapper.UserWordMapper;
import com.ruoyi.system.gencode.service.LexiconService;
import com.ruoyi.system.gencode.service.LexiconWordService;
import com.ruoyi.system.gencode.service.UserWordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.gencode.service.WordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户单词表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Service
public class UserWordServiceImpl extends ServiceImpl<UserWordMapper, UserWord> implements UserWordService {
    @Autowired
    private UserWordMapper userWordMapper;

    @Autowired
    private WordService wordService;

    @Autowired
    private LexiconWordService lexiconWordService;

    @Autowired
    private LexiconService lexiconService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int collect(String wordUUID, Long userId, String userName) {
        QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
        wordQueryWrapper.eq("uuid", wordUUID);
        Word word = wordService.getOne(wordQueryWrapper);
        Assert.notNull(word, "请求参数错误");
        // 检查一下是否有重复的
        QueryWrapper<UserWord> userWordQueryWrapper = new QueryWrapper<>();
        userWordQueryWrapper.eq("word_uuid", word.getUuid());
        UserWord repeatWord = userWordMapper.selectOne(userWordQueryWrapper);
        if (Objects.nonNull(repeatWord)) {
            repeatWord.setCollectFlag(1);
            return userWordMapper.updateById(repeatWord);
        }
        UserWord userWord = new UserWord()
                .setCollectFlag(1)
                .setWord(word.getWord())
                .setWordUuid(word.getUuid())
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
        userWordQueryWrapper.eq("word_uuid", userWord.getWordUuid());
        UserWord repeatWord = userWordMapper.selectOne(userWordQueryWrapper);
        if (Objects.nonNull(repeatWord)) {
            setPeriodAndNextTime(repeatWord, repeatWord.getPeriod() + 1);
            return userWordMapper.updateById(repeatWord);
        }
        setPeriodAndNextTime(userWord, 1);
        userWord.setCollectFlag(0);
        userWord.setUuid(UUID.randomUUID().toString().replace("-",""));
        return userWordMapper.insert(userWord);
    }

    @Override
    public WordShowData getNeedReviewWord(Long userId) {
        Word needReviewWord = userWordMapper.getNeedReviewWord(DateUtil.now(), userId);
        if (Objects.isNull(needReviewWord)) {
            return null;
        }
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(needReviewWord, wordShowData);
        // 根据单词查询词库uuid
        QueryWrapper<LexiconWord> lexiconWordQueryWrapper = new QueryWrapper<>();
        lexiconWordQueryWrapper.eq("word_uuid", needReviewWord.getUuid());
        List<LexiconWord> lexiconWords = lexiconWordService.list(lexiconWordQueryWrapper);
        if (CollectionUtils.isEmpty(lexiconWords)) {
            return wordShowData;
        }
        List<Label> labelOfLexicon = lexiconService.getLabelOfLexicon(lexiconWords.get(0).getLexiconUuid());
        if (!CollectionUtils.isEmpty(labelOfLexicon)) {
            List<String> labelNames = labelOfLexicon.stream().map(Label::getName).collect(Collectors.toList());
            wordShowData.setLabelList(labelNames);
        }
        return wordShowData;
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
