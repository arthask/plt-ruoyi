package com.ruoyi.system.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.file.ParseUtils;
import com.ruoyi.system.domain.Word;
import com.ruoyi.system.domain.dto.WordShowData;
import com.ruoyi.system.mapper.OldWordMapper;
import com.ruoyi.system.service.IWordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单词Service业务层处理
 *
 * @author ruoyi
 * @date 2023-12-28
 */
@Service("oldWorldService")
public class WordServiceImpl implements IWordService {
    private static final Logger log = LoggerFactory.getLogger(WordServiceImpl.class);
    @Autowired
    private OldWordMapper oldWordMapper;

    /**
     * 查询单词
     *
     * @param id 单词主键
     * @return 单词
     */
    @Override
    public Word selectWordById(Long id) {
        return oldWordMapper.selectWordById(id);
    }

    /**
     * 查询单词列表
     *
     * @param word 单词
     * @return 单词
     */
    @Override
    public List<Word> selectWordList(Word word) {
        return oldWordMapper.selectWordList(word);
    }

    /**
     * 新增单词
     *
     * @param word 单词
     * @return 结果
     */
    @Override
    public int insertWord(Word word) {
        return oldWordMapper.insertWord(word);
    }

    /**
     * 修改单词
     *
     * @param word 单词
     * @return 结果
     */
    @Override
    public int updateWord(Word word) {
        return oldWordMapper.updateWord(word);
    }

    /**
     * 批量删除单词
     *
     * @param ids 需要删除的单词主键
     * @return 结果
     */
    @Override
    public int deleteWordByIds(Long[] ids) {
        return oldWordMapper.deleteWordByIds(ids);
    }

    /**
     * 删除单词信息
     *
     * @param id 单词主键
     * @return 结果
     */
    @Override
    public int deleteWordById(Long id) {
        return oldWordMapper.deleteWordById(id);
    }

    @Override
    public String importWords(List<Word> wordList, Boolean isUpdateSupport, Long userId) {
        if (CollectionUtils.isEmpty(wordList)) {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Word word : wordList) {
            try {
                word.setCreateUserId(userId);
                // 验证是否存在这个单词
                Word wordFromDb = oldWordMapper.selectWordByName(word.getWord(), userId);
                if (Objects.isNull(wordFromDb)) {
                    oldWordMapper.insertWord(word);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + word.getWord() + " 导入成功");
                } else if (isUpdateSupport) {
                    word.setId(wordFromDb.getId());
                    oldWordMapper.updateWord(word);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + word.getWord() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + word.getWord() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + word.getWord() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Override
    public WordShowData getOneWord(Long userId, Long lexiconId, int index) {
//        Long count = wordMapper.getNewWordCountOfUser(userId);
//        long offset = RandomUtils.nextLong(0, count);
        Word word = oldWordMapper.getRandomWordOfUser(userId,lexiconId, (long) index);
        Assert.notNull(word,"未查询到单词数据");
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(word, wordShowData);
        return wordShowData;
    }

    @Override
    public List<Word> getNewWordOfUser(Long userId) {
        return oldWordMapper.getNewWordOfUser(userId);
    }

    @Override
    public List<WordShowData> searchWordByCN(String searchCn) {
        List<Word> wordList = oldWordMapper.searchWordByCN(searchCn);
        // 查询单词对应的词库

        List<WordShowData> resultList = Optional.ofNullable(wordList)
                .orElse(Collections.emptyList())
                .stream().map(e -> {
                    WordShowData wordShowData = new WordShowData();
                    BeanUtils.copyProperties(e, wordShowData);
                    return wordShowData;
                }).collect(Collectors.toList());

        return null;
    }

    @Override
    public String parseJsonFormatWordFile(File file, Long userId) {
        try {
            String jsonStr = ParseUtils.parseJsonFile(file);
            JSONArray objects = JSONUtil.parseArray(jsonStr);
            List<Word> wordList = new ArrayList<>();
            objects.forEach(e -> {
                JSONObject object = (JSONObject) e;
                String name = object.getStr("name");
                String trans = object.getJSONArray("trans").toString();
                Word word = new Word();
                word.setWord(name);
                word.setTranslation(trans);
                word.setCreateUserId(userId);
                wordList.add(word);
            });
            if (!CollectionUtils.isEmpty(wordList)) {
                oldWordMapper.insertBatch(wordList);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public JSONArray parseUploadFile(MultipartFile file) {
        try {
            String jsonStr = ParseUtils.parseUploadFile(file);
            return JSONUtil.parseArray(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
