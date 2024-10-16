package com.example.pltool.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.note.NoteDto;
import com.example.pltool.domain.entity.Note;
import com.example.pltool.domain.entity.Question;
import com.example.pltool.domain.vo.NoteInfoVo;
import com.example.pltool.mapper.NoteMapper;
import com.example.pltool.mapper.QuestionMapper;
import com.example.pltool.service.NoteService;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * <p>
 * 笔记表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

  @Autowired
  private QuestionMapper questionMapper;

  @Override
  public NoteInfoVo getQuestionNoteInfo(String noteUUId) {
    NoteInfoVo noteInfoVo = new NoteInfoVo();
    LambdaQueryWrapper<Note> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(Note::getUuid, noteUUId);
    Note note = this.getOne(lambdaQueryWrapper);
    QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("note_id", note.getId());
    List<Question> questions = questionMapper.selectList(queryWrapper);
    List<Question> resultQuestions = new ArrayList<>();
    questions.forEach(e -> {
      Question question = new Question();
      question.setQuestion(e.getQuestion());
      question.setAnswer(e.getAnswer());
      question.setUuid(e.getUuid());
      question.setNoteId(e.getNoteId());
      resultQuestions.add(question);
    });
    noteInfoVo.setNote(note);
    noteInfoVo.setQuestionList(resultQuestions);
    return noteInfoVo;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult updateNoteInfo(NoteInfoVo noteInfoVo) {
    QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("uuid", noteInfoVo.getNote().getUuid());
    Note note = getBaseMapper().selectOne(queryWrapper);
    boolean updateFlag = false;
    if (!StringUtils.equals(note.getTitle(), noteInfoVo.getNote().getTitle())) {
      note.setTitle(noteInfoVo.getNote().getTitle());
      updateFlag = true;
    }
    if (StringUtils.isNotBlank(noteInfoVo.getNote().getSummary())) {
      note.setSummary(noteInfoVo.getNote().getSummary());
      updateFlag = true;
    }
    if (StringUtils.isNotBlank(noteInfoVo.getNote().getContent())) {
      note.setContent(noteInfoVo.getNote().getContent());
      updateFlag = true;
    }
    if (updateFlag) {
      getBaseMapper().updateById(note);
    }
    if (!CollectionUtils.isEmpty(noteInfoVo.getQuestionList())) {
      List<Question> questionList = noteInfoVo.getQuestionList();
      questionList.forEach(e -> e
          .setUpdateTime(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())));
      questionMapper.updateQuestionBatch(questionList);
    }
    return AjaxResult.success(true);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult saveOrUpdateNote(NoteDto noteDto) {
    if (StringUtils.isNotBlank(noteDto.getUuid())) {
      LambdaUpdateWrapper<Note> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
      lambdaQueryWrapper.eq(Note::getUuid, noteDto.getUuid()).set(Note::getContent,
          noteDto.getContent());
      update(lambdaQueryWrapper);
    } else {
      Note note = new Note();
      note.setTitle(noteDto.getTitle());
      note.setUserId(noteDto.getUserId());
      String uuid = UUID.randomUUID().toString().replace("-", "");
      note.setUuid(uuid);
      note.setType(noteDto.getType());
      note.setContent(noteDto.getContent());
      if (StringUtils.isNotBlank(noteDto.getSummary())) {
        note.setSummary(noteDto.getSummary());
      }
      if (StringUtils.isNotBlank(noteDto.getRefUUId())) {
        note.setRefUuid(noteDto.getRefUUId());
      }
      save(note);
    }
    return AjaxResult.success(true);
  }

  @Override
  public AjaxResult getNoteInfoByRefUUId(String refUUId) {
    LambdaQueryWrapper<Note> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.eq(Note::getRefUuid, refUUId);
    Note note = getOne(lambdaQueryWrapper);
    return AjaxResult.success(note);
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public AjaxResult removeByUUIdList(List<String> uuidList) {
    LambdaQueryWrapper<Note> lambdaQueryWrapper = new LambdaQueryWrapper<>();
    lambdaQueryWrapper.in(Note::getUuid, uuidList);
    boolean removeFlag = remove(lambdaQueryWrapper);
    if (removeFlag) {
      return AjaxResult.success(true);
    }
    return AjaxResult.error("删除失败", false);
  }
}
