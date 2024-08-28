package com.example.pltool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.WordSentence;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 单词例句表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-08-28
 */
@Mapper
public interface WordSentenceMapper extends BaseMapper<WordSentence> {

}
