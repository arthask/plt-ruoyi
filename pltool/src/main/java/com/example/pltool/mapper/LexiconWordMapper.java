package com.example.pltool.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.LexiconWord;

/**
 * <p>
 * 词库与单词关系表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-02
 */
@Mapper
public interface LexiconWordMapper extends BaseMapper<LexiconWord> {

}
