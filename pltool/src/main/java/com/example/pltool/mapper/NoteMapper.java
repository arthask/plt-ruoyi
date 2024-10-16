package com.example.pltool.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Note;

/**
 * <p>
 * 笔记表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-09-04
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {

}
