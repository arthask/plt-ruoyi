package com.example.pltool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.pltool.domain.entity.Word;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 单词表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Mapper
public interface WordMapper extends BaseMapper<Word> {
    /**
     * 使用中文操作单词
     * @param searchCn 中文搜索内容
     * @return 匹配的单词
     */
    List<Word> searchWordByCN(@Param("searchCn") String searchCn);

    /**
     * 随机获取用户词库的一个单词
     * @param userId 用户id
     * @return
     */
    Word getRandomWordOfUser(@Param("userId") Long userId, @Param("lexiconUUID") String lexiconUUID, @Param("offset") Long offset);

    /**
     * 查询用户未学习的新单词总数
     * @param userId 用户id
     * @return 未学习的新单词总数
     */
    Long getNewWordCountOfUser(@Param("userId") Long userId);

    /**
     * 查询单词总数量
     * @param userId 用户id
     * @return 单词总数量
     */
    Long getWordCount(@Param("userId") Long userId);
}
