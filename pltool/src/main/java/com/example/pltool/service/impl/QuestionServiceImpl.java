package com.example.pltool.service.impl;

import static com.ruoyi.common.constant.Constants.IS_SUCCESS;
import static com.ruoyi.common.constant.Constants.NOTE_UUID;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.note.QuestionDto;
import com.example.pltool.domain.entity.Note;
import com.example.pltool.domain.entity.Question;
import com.example.pltool.mapper.NoteMapper;
import com.example.pltool.mapper.QuestionMapper;
import com.example.pltool.service.QuestionService;

/**
 * <p>
 * 问题表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-03
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

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
    String uuid = UUID.randomUUID().toString().replace("-", "");
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
