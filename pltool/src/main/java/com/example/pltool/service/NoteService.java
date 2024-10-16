package com.example.pltool.service;

import java.util.List;

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

  NoteInfoVo getQuestionNoteInfo(String noteUUId);

  AjaxResult updateNoteInfo(NoteInfoVo noteInfoVo);

  AjaxResult saveOrUpdateNote(NoteDto noteDto);

  AjaxResult getNoteInfoByRefUUId(String refUUId);

  AjaxResult removeByUUIdList(List<String> uuidList);

}
