package com.example.pltool.service;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.language.word.WordShowData;
import com.example.pltool.domain.entity.Word;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 * <p>
 * 单词表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
public interface WordService extends IService<Word> {

    /**
     * 使用中文操作单词
     *
     * @param searchCn 中文搜索内容
     * @return 匹配的单词
     */
    List<WordShowData> searchWordByCN(String searchCn);

    List<WordShowData> searchWord(String word);

    WordShowData getOneWord(Long userId, String lexiconUUID, int index);

    WordShowData getWordInfo(String wordUuid);

    Word getWordByUUID(String uuid);

    JSONArray parseUploadFile(MultipartFile file);

    /**
     * 导入单词
     *
     * @param wordList        单词列表
     * @param isUpdateSupport 是否支持更新
     * @return 导入结果信息
     */
    String importWords(List<Word> wordList, Boolean isUpdateSupport, Long userId);
}
