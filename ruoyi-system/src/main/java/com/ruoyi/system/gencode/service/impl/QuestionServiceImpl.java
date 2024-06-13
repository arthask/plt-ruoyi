package com.ruoyi.system.gencode.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.dto.QuestionDto;
import com.ruoyi.system.domain.dto.WordShowData;
import com.ruoyi.system.gencode.entity.Note;
import com.ruoyi.system.gencode.entity.Question;
import com.ruoyi.system.gencode.entity.Word;
import com.ruoyi.system.gencode.mapper.NoteMapper;
import com.ruoyi.system.gencode.mapper.QuestionMapper;
import com.ruoyi.system.gencode.service.QuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.ruoyi.common.constant.Constants.IS_SUCCESS;
import static com.ruoyi.common.constant.Constants.NOTE_UUID;

/**
 * <p>
 * 问题表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NoteMapper noteMapper;
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
        String uuid = UUID.randomUUID().toString().replace("-","");
        note.setUuid(uuid);
        int affectRow = noteMapper.insert(note);
        if (affectRow > 0) {
            QueryWrapper<Note> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", uuid);
            Note noteFromDb = noteMapper.selectOne(queryWrapper);
            List<Question> questions = questionDto.getQuestions();
            questions.forEach(e -> {
                e.setUserId(userId);
                e.setUuid(UUID.randomUUID().toString().replace("-", ""));
                e.setNoteId(noteFromDb.getId());
            });
            resultMap.put(IS_SUCCESS, this.saveBatch(questions));
            resultMap.put(NOTE_UUID, uuid);
            return resultMap;
        }
        return resultMap;
    }

    @Override
    public Question getByUUID(String uuid) {
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("uuid", uuid);
        return getOne(query);
    }

    @Override
    public List<Question> searchQuestion(String question) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("question", question);
        List<Question> questionList = list(queryWrapper);
        if (CollectionUtils.isEmpty(questionList)) {
            return Collections.emptyList();
        }
        return questionList;
    }
}
