package com.ruoyi.system.gencode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.domain.dto.WordShowData;
import com.ruoyi.system.gencode.entity.*;
import com.ruoyi.system.gencode.mapper.LexiconWordMapper;
import com.ruoyi.system.gencode.mapper.WordMapper;
import com.ruoyi.system.gencode.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 单词表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Service("wordService")
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private LexiconWordService lexiconWordService;

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private LabelService labelService;

    @Override
    public List<WordShowData> searchWordByCN(String searchCn) {
        List<Word> wordList = wordMapper.searchWordByCN(searchCn);
        if (CollectionUtils.isEmpty(wordList)) {
            return Collections.emptyList();
        }
        // 查询单词对应的词库
        List<String> wordUUIDList = wordList
                .stream()
                .map(Word::getUuid)
                .collect(Collectors.toList());

        QueryWrapper<LexiconWord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("word_uuid", wordUUIDList);
        List<LexiconWord> lexiconWords = lexiconWordService.list(queryWrapper);

        Map<String, List<LexiconWord>> lexiconWordGroupByLexiconUUID = lexiconWords.stream()
                .collect(Collectors.groupingBy(LexiconWord::getLexiconUuid));

        Set<String> lexiconUUIDSet = lexiconWordGroupByLexiconUUID.keySet();

        Map<String, List<Lexicon>> lexiconMapGroupByUUID = new HashMap<>();
        Map<String, List<LabelRef>> labelRefGroupByLexiconUUID = new HashMap<>();
        Map<String, List<LexiconWord>> lexiconWordGroupByWordUUID = new HashMap<>();
        Map<String, List<Label>> labelGroupByUUID = new HashMap<>();

        if (!CollectionUtils.isEmpty(lexiconUUIDSet)) {
            QueryWrapper<Lexicon> lexiconQueryWrapper = new QueryWrapper<>();
            lexiconQueryWrapper.in("uuid", lexiconUUIDSet);
            List<Lexicon> lexicons = lexiconService.list(lexiconQueryWrapper);
            lexiconMapGroupByUUID.putAll(lexicons.stream()
                    .collect(Collectors.groupingBy(Lexicon::getUuid)));

            // 词库的标签
            QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
            labelRefQueryWrapper.in("ref_uuid", lexiconUUIDSet);
            List<LabelRef> labelRefList = labelRefService.list(labelRefQueryWrapper);
            Map<String, List<LabelRef>> labelRefGroupByLabelUUID = labelRefList.stream()
                    .collect(Collectors.groupingBy(LabelRef::getLabelUuid));
            labelRefGroupByLexiconUUID.putAll(labelRefList.stream()
                    .collect(Collectors.groupingBy(LabelRef::getRefUuid)));
            if (!CollectionUtils.isEmpty(labelRefGroupByLabelUUID)) {
                QueryWrapper<Label> labelQueryWrapper = new QueryWrapper<>();
                labelQueryWrapper.in("uuid", labelRefGroupByLabelUUID.keySet());
                List<Label> labelList = labelService.list(labelQueryWrapper);
                labelGroupByUUID.putAll(labelList.stream()
                        .collect(Collectors.groupingBy(Label::getUuid)));
            }
            lexiconWordGroupByWordUUID.putAll(lexiconWords.stream()
                    .collect(Collectors.groupingBy(LexiconWord::getWordUuid)));
        }

        return wordList.stream().map(e -> {
            List<String> lexiconNameList = new ArrayList<>();
            List<String> labelNameList = new ArrayList<>();
            List<LexiconWord> lexiconWordList = lexiconWordGroupByWordUUID.get(e.getUuid());
            if (lexiconWordGroupByWordUUID.containsKey(e.getUuid())) {
                List<String> lexiconUUIDList = lexiconWordList.stream()
                        .map(LexiconWord::getLexiconUuid).collect(Collectors.toList());
                lexiconUUIDList.forEach(l -> {
                    if (lexiconMapGroupByUUID.containsKey(l)) {
                        lexiconNameList.add(lexiconMapGroupByUUID.get(l).get(0).getName());
                    }
                    if (labelRefGroupByLexiconUUID.containsKey(l)) {
                        String labelUuid = labelRefGroupByLexiconUUID.get(l).get(0).getLabelUuid();
                        labelNameList.add(labelGroupByUUID.get(labelUuid).get(0).getName());
                    }
                });
            }
            WordShowData wordShowData = new WordShowData();
            BeanUtils.copyProperties(e, wordShowData);
            if (!CollectionUtils.isEmpty(lexiconNameList)) {
                wordShowData.setLexiconName(lexiconNameList);
            }
            if (!CollectionUtils.isEmpty(labelNameList)) {
                wordShowData.setLabelList(labelNameList);
            }
            return wordShowData;
        }).collect(Collectors.toList());
    }

    @Override
    public WordShowData getOneWord(Long userId, String lexiconUUID, int index) {
        // 查询词库标签
        Word word = wordMapper.getRandomWordOfUser(userId, lexiconUUID, (long) index);
        Assert.notNull(word, "未查询到单词数据");
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(word, wordShowData);
        List<Label> labelOfLexicon = lexiconService.getLabelOfLexicon(lexiconUUID);
        if (!CollectionUtils.isEmpty(labelOfLexicon)) {
            List<String> labelNames = labelOfLexicon.stream().map(Label::getName).collect(Collectors.toList());
            wordShowData.setLabelList(labelNames);
        }
        return wordShowData;
    }

    @Override
    public WordShowData getWordInfo(String wordUuid) {
        // 查询词库标签
        Word word = getWordByUUID(wordUuid);
        Assert.notNull(word, "未查询到单词数据");
        WordShowData wordShowData = new WordShowData();
        BeanUtils.copyProperties(word, wordShowData);
        // 根据单词查询词库uuid
        QueryWrapper<LexiconWord> lexiconWordQueryWrapper = new QueryWrapper<>();
        lexiconWordQueryWrapper.eq("word_uuid", word.getUuid());
        List<LexiconWord> lexiconWords = lexiconWordService.list(lexiconWordQueryWrapper);
        if (CollectionUtils.isEmpty(lexiconWords)) {
            return wordShowData;
        }
        List<Label> labelOfLexicon = lexiconService.getLabelOfLexicon(lexiconWords.get(0).getLexiconUuid());
        if (!CollectionUtils.isEmpty(labelOfLexicon)) {
            List<String> labelNames = labelOfLexicon.stream().map(Label::getName).collect(Collectors.toList());
            wordShowData.setLabelList(labelNames);
        }
        return wordShowData;
    }

    @Override
    public Word getWordByUUID(String uuid) {
        QueryWrapper<Word> wordQuery = new QueryWrapper<>();
        wordQuery.eq("uuid", uuid);
        return getOne(wordQuery);
    }

    @Override
    public List<WordShowData> searchWord(String word) {
        QueryWrapper<Word> wordQuery = new QueryWrapper<>();
        wordQuery.like("word", word);
        List<Word> wordList = list(wordQuery);
        if (CollectionUtils.isEmpty(wordList)) {
            return Collections.emptyList();
        }
        List<WordShowData> wordShowDataList = new ArrayList<>();
        wordList.forEach(e -> {
            WordShowData wordShowData = new WordShowData();
            BeanUtils.copyProperties(e, wordShowData);
            wordShowDataList.add(wordShowData);
        });
        return wordShowDataList;
    }
}
