package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.WordShowData;
import com.ruoyi.system.gencode.entity.Word;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

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
     * @param searchCn 中文搜索内容
     * @return 匹配的单词
     */
    List<WordShowData> searchWordByCN(@Param("searchCn") String searchCn);

    WordShowData getOneWord(Long userId, String lexiconUUID, int index);

    WordShowData getWordInfo(String wordUuid);
}
