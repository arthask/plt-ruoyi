package com.example.pltool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.pltool.domain.entity.Note;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 笔记表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {

}
