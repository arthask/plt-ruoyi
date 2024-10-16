package com.example.pltool.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pltool.domain.entity.Dialogue;

/**
 * <p>
 * 对话表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-06-10
 */
@Mapper
public interface DialogueMapper extends BaseMapper<Dialogue> {

}
