package com.ruoyi.system.gencode.mapper;

import com.ruoyi.system.gencode.entity.Word;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
