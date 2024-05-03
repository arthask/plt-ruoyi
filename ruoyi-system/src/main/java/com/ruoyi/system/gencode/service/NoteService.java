package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.vo.NoteInfoVo;
import com.ruoyi.system.gencode.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 笔记表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
public interface NoteService extends IService<Note> {

    NoteInfoVo getNoteInfo(Long id);

    int updateNoteInfo(NoteInfoVo noteInfoVo);

}
