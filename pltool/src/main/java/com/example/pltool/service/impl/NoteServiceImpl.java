package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.note.NoteDto;
import com.example.pltool.domain.entity.Note;
import com.example.pltool.domain.entity.Question;
import com.example.pltool.domain.vo.NoteInfoVo;
import com.example.pltool.mapper.NoteMapper;
import com.example.pltool.mapper.QuestionMapper;
import com.example.pltool.service.NoteService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    public NoteInfoVo getNoteInfo(Long id) {
        NoteInfoVo noteInfoVo = new NoteInfoVo();
        Note note = baseMapper.selectById(id);
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
    public int updateNoteInfo(NoteInfoVo noteInfoVo) {
        QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", noteInfoVo.getNote().getUuid());
        Note note = getBaseMapper().selectOne(queryWrapper);
        note.setTitle(noteInfoVo.getNote().getTitle());
        note.setSummary(noteInfoVo.getNote().getSummary());
        getBaseMapper().updateById(note);
        List<Question> questionList = noteInfoVo.getQuestionList();
        questionList.forEach(e -> e.setUpdateTime(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())));
        return questionMapper.updateQuestionBatch(questionList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult addNote(NoteDto noteDto) {
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setUserId(noteDto.getUserId());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        note.setUuid(uuid);
        note.setType(noteDto.getType());
        note.setContent(noteDto.getContent());
        if (StringUtils.isNotBlank(noteDto.getRefUUId())) {
            note.setSummary(noteDto.getSummary());
        }
        if (StringUtils.isNotBlank(noteDto.getRefUUId())) {
            note.setRefUuid(noteDto.getRefUUId());
        }
        save(note);
        return AjaxResult.success(true);
    }
}
