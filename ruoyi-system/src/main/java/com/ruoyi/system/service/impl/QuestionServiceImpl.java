package com.ruoyi.system.service.impl;

import cn.hutool.core.lang.UUID;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Note;
import com.ruoyi.system.domain.Question;
import com.ruoyi.system.domain.dto.QuestionDto;
import com.ruoyi.system.mapper.NoteMapper;
import com.ruoyi.system.mapper.QuestionMapper;
import com.ruoyi.system.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruoyi.common.constant.Constants.IS_SUCCESS;
import static com.ruoyi.common.constant.Constants.NOTE_UUID;

/**
 * 问题Service业务层处理
 *
 * @author ruoyi
 * @date 2024-02-02
 */
@Service
public class QuestionServiceImpl implements IQuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NoteMapper noteMapper;

    /**
     * 查询问题
     *
     * @param id 问题主键
     * @return 问题
     */
    @Override
    public Question selectQuestionById(Long id) {
        return questionMapper.selectQuestionById(id);
    }

    /**
     * 查询问题列表
     *
     * @param question 问题
     * @return 问题
     */
    @Override
    public List<Question> selectQuestionList(Question question) {
        return questionMapper.selectQuestionList(question);
    }

    /**
     * 新增问题
     *
     * @param questionDto 问题
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> insertQuestion(QuestionDto questionDto, Long userId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(IS_SUCCESS, false);
        Note note = new Note();
        note.setTitle(questionDto.getNoteTitle());
        note.setUserId(userId);
        String uuid = UUID.randomUUID().toString();
        note.setUuid(uuid);
        int affectRow = noteMapper.insertNote(note);
        if (affectRow > 0) {
            Note noteFromDb = noteMapper.selectNoteByUUId(uuid);
            List<Question> questions = questionDto.getQuestions();
            questions.forEach(e -> {
                e.setUserId(userId);
                e.setUuid(UUID.randomUUID().toString());
                e.setNoteId(noteFromDb.getId());
            });
            resultMap.put(IS_SUCCESS, questionMapper.insertQuestionBatch(questions) > 0);
            resultMap.put(NOTE_UUID, uuid);
            return resultMap;
        }
        return resultMap;
    }

    /**
     * 修改问题
     *
     * @param question 问题
     * @return 结果
     */
    @Override
    public int updateQuestion(Question question) {
        question.setUpdateTime(DateUtils.getNowDate());
        return questionMapper.updateQuestion(question);
    }

    /**
     * 批量删除问题
     *
     * @param ids 需要删除的问题主键
     * @return 结果
     */
    @Override
    public int deleteQuestionByIds(Long[] ids) {
        return questionMapper.deleteQuestionByIds(ids);
    }

    /**
     * 删除问题信息
     *
     * @param id 问题主键
     * @return 结果
     */
    @Override
    public int deleteQuestionById(Long id) {
        return questionMapper.deleteQuestionById(id);
    }
}
