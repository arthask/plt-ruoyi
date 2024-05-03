package com.ruoyi.system.gencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.vo.NoteInfoVo;
import com.ruoyi.system.gencode.entity.Note;
import com.ruoyi.system.gencode.entity.Question;
import com.ruoyi.system.gencode.mapper.NoteMapper;
import com.ruoyi.system.gencode.mapper.QuestionMapper;
import com.ruoyi.system.gencode.service.NoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
}
