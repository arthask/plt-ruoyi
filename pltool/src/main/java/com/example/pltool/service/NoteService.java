package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.note.NoteDto;
import com.example.pltool.domain.entity.Note;
import com.example.pltool.domain.vo.NoteInfoVo;
import com.ruoyi.common.core.domain.AjaxResult;


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

    AjaxResult addNote(NoteDto noteDto);

}
