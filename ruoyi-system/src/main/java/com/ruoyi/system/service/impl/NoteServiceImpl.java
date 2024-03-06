package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Note;
import com.ruoyi.system.domain.Question;
import com.ruoyi.system.domain.vo.NoteInfoVo;
import com.ruoyi.system.mapper.NoteMapper;
import com.ruoyi.system.mapper.QuestionMapper;
import com.ruoyi.system.service.INoteService;
import com.ruoyi.system.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 笔记Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-02-02
 */
@Service
public class NoteServiceImpl implements INoteService {
    @Autowired
    private IQuestionService questionService;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private QuestionMapper questionMapper;

    /**
     * 查询笔记
     * 
     * @param id 笔记主键
     * @return 笔记
     */
    @Override
    public Note selectNoteById(Long id)
    {
        return noteMapper.selectNoteById(id);
    }

    /**
     * 查询笔记列表
     * 
     * @param note 笔记
     * @return 笔记
     */
    @Override
    public List<Note> selectNoteList(Note note)
    {
        return noteMapper.selectNoteList(note);
    }

    /**
     * 新增笔记
     * 
     * @param note 笔记
     * @return 结果
     */
    @Override
    public int insertNote(Note note) {
        note.setCreateTime(DateUtils.getNowDate());
        return noteMapper.insertNote(note);
    }

    /**
     * 修改笔记
     * 
     * @param note 笔记
     * @return 结果
     */
    @Override
    public int updateNote(Note note)
    {
        note.setUpdateTime(DateUtils.getNowDate());
        return noteMapper.updateNoteByUUID(note);
    }

    /**
     * 批量删除笔记
     * 
     * @param ids 需要删除的笔记主键
     * @return 结果
     */
    @Override
    public int deleteNoteByIds(Long[] ids)
    {
        return noteMapper.deleteNoteByIds(ids);
    }

    /**
     * 删除笔记信息
     * 
     * @param id 笔记主键
     * @return 结果
     */
    @Override
    public int deleteNoteById(Long id) {
        return noteMapper.deleteNoteById(id);
    }

    @Override
    public NoteInfoVo getNoteInfo(Long id) {
        NoteInfoVo noteInfoVo = new NoteInfoVo();
        Note note = noteMapper.selectNoteById(id);
        List<Question> questions = questionMapper.selectByNoteId(id);
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
        Note note = noteMapper.selectNoteByUUId(noteInfoVo.getNote().getUuid());
        note.setTitle(noteInfoVo.getNote().getTitle());
        note.setSummary(noteInfoVo.getNote().getSummary());
        noteMapper.updateNote(note);
        List<Question> questionList = noteInfoVo.getQuestionList();
        questionList.forEach(e -> e.setUpdateTime(new Date()));
        return questionMapper.updateQuestionBatch(questionList);
    }
}
