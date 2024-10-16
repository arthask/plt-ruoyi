package com.example.pltool.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Lexicon;

/**
 * <p>
 * 词库表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Mapper
public interface LexiconMapper extends BaseMapper<Lexicon> {

}
