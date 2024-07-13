package com.example.pltool.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.entity.*;

import com.example.pltool.mapper.LexiconMapper;
import com.example.pltool.service.*;
import com.example.pltool.domain.dto.language.lexicon.LexiconData;
import com.example.pltool.domain.dto.language.lexicon.LexiconShowData;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 词库表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
@Service
public class LexiconServiceImpl extends ServiceImpl<LexiconMapper, Lexicon> implements LexiconService {

    @Autowired
    private WordService wordService;

    @Autowired
    private LexiconService lexiconService;

    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private WordService wordNewService;


    @Autowired
    private LexiconWordService lexiconWordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createLexicon(LexiconData lexiconData) {
        Lexicon insertLexicon = new Lexicon();
        String lexiconUUID = UUID.randomUUID().toString();
        insertLexicon.setUuid(lexiconUUID);
        insertLexicon.setName(lexiconData.getName());
        insertLexicon.setLanguage(lexiconData.getLanguage());
        insertLexicon.setCreateUserId(lexiconData.getUserId());
        lexiconService.save(insertLexicon);

        List<Label> labels = new ArrayList<>();
        List<String> labelUUIDList = new ArrayList<>();
        lexiconData.getLabelList().forEach(e -> {
            Label label = new Label();
            label.setName(e);
            String labelUUID = UUID.randomUUID().toString();
            label.setUuid(labelUUID);
            label.setCreateUserId(lexiconData.getUserId());
            labelUUIDList.add(labelUUID);
            labels.add(label);
        });
        if (!CollectionUtils.isEmpty(labels)) {
            labelService.saveBatch(labels);
        }

        JSONArray wordJsonArray = wordService.parseUploadFile(lexiconData.getFile());
        List<Word> wordList = new ArrayList<>();
        List<String> wordUUIDList = new ArrayList<>();
        wordJsonArray.forEach(e -> {
            JSONObject object = (JSONObject) e;
            String name = object.getStr("name");
            String trans = object.getJSONArray("trans").toString();
            Word word = new Word();
            word.setUuid(UUID.randomUUID().toString());
            word.setWord(name);
            word.setTranslation(trans);
            word.setCreateUserId(lexiconData.getUserId());
            wordUUIDList.add(word.getUuid());
            wordList.add(word);
        });

        if (!CollectionUtils.isEmpty(wordList)) {
            wordNewService.saveBatch(wordList);
        }

        if (!CollectionUtils.isEmpty(labelUUIDList)) {
            List<LabelRef> labelRefList = labelUUIDList.stream()
                    .map(e -> {
                        LabelRef labelRef = new LabelRef();
                        labelRef.setUuid(UUID.randomUUID().toString());
                        // 0表示词库，1表示单词
                        labelRef.setRefType(0);
                        labelRef.setRefUuid(insertLexicon.getUuid());
                        labelRef.setLabelUuid(e);
                        labelRef.setCreateUserId(lexiconData.getUserId());
                        return labelRef;
                    })
                    .collect(Collectors.toList());
            labelRefService.saveBatch(labelRefList);
        }

        if (!CollectionUtils.isEmpty(wordUUIDList)) {
            List<LexiconWord> lexiconWordList = wordUUIDList.stream()
                    .map(e -> {
                        LexiconWord lexiconWord = new LexiconWord();
                        lexiconWord.setUuid(UUID.randomUUID().toString());
                        lexiconWord.setWordUuid(e);
                        lexiconWord.setLexiconUuid(lexiconUUID);
                        return lexiconWord;
                    })
                    .collect(Collectors.toList());
            lexiconWordService.saveBatch(lexiconWordList);
        }
        return "创建成功";
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String removeLexicon(Long[] ids) {
        Assert.notEmpty(ids, "请求参数错误");
        List<Lexicon> lexicons = lexiconService.listByIds(Arrays.asList(ids));
        if (CollectionUtils.isEmpty(lexicons)) {
            return "删除失败";
        }
        List<String> lexiconUUIdList = lexicons.stream()
                .map(Lexicon::getUuid)
                .collect(Collectors.toList());
        // 删除词库下的单词
        QueryWrapper<LexiconWord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("lexion_uuid", lexiconUUIdList);
        lexiconWordService.remove(queryWrapper);
        // 删除词库
        lexiconService.removeBatchByIds(Arrays.asList(ids));
        // 删除词库与标签的关联关系
        QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
        labelRefQueryWrapper.in("ref_uuid", lexiconUUIdList);
        labelRefService.remove(labelRefQueryWrapper);
        return "删除成功";
    }

    @Override
    public LexiconShowData getInfo(Long id) {
        Lexicon lexicon = getById(id);
        Assert.notNull(lexicon, "未获取到词库信息");
        LexiconShowData lexiconData = new LexiconShowData();
        BeanUtils.copyProperties(lexicon, lexiconData);
        Map<String, List<Label>> labelNameMap = getLabelNameList(Collections.singletonList(lexicon));
        List<Label> labelNameList = labelNameMap.get(lexicon.getUuid());
        lexiconData.setLabelList(labelNameList);
        return lexiconData;
    }

    @Override
    public List<LexiconShowData> convertLexiconList(List<Lexicon> lexicons) {
        if (CollectionUtils.isEmpty(lexicons)) {
            return Collections.emptyList();
        }
        List<LexiconShowData> result = new ArrayList<>();
        Optional.ofNullable(lexicons).ifPresent(e -> {
            List<LexiconShowData> lexiconDataList = e.stream()
                    .map(lex -> {
                        LexiconShowData lexiconData = new LexiconShowData();
                        BeanUtils.copyProperties(lex, lexiconData);
                        return lexiconData;
                    }).collect(Collectors.toList());

            Map<String, List<Label>> labelNameMap = getLabelNameList(lexicons);
            lexiconDataList.forEach(l -> {
                l.setLabelList(labelNameMap.get(l.getUuid()));
            });
            result.addAll(lexiconDataList);
        });
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updateLexicon(LexiconShowData lexiconShowData) {
        // 更新词库
        Lexicon updateLexicon = new Lexicon();
        BeanUtils.copyProperties(lexiconShowData, updateLexicon);
        lexiconService.updateById(updateLexicon);
        // 更新词库对应的label标签，更新名称
        List<Label> labelList = lexiconShowData.getLabelList();
        // 需要新增的标签
        List<Label> needAddLabelList = new ArrayList<>();
        List<Label> needUpadateLabelList = new ArrayList<>();
        labelList.forEach(e -> {
            if (Objects.nonNull(e.getId())) {
                needUpadateLabelList.add(e);
            } else {
                e.setUuid(UUID.randomUUID().toString().replace("-",""));
                e.setCreateUserId(lexiconShowData.getCreateUserId());
                needAddLabelList.add(e);
            }
        });
        // 需要更新的标签
        if (!CollectionUtils.isEmpty(needUpadateLabelList)) {
            labelService.updateBatchById(needUpadateLabelList);
        }
        if (!CollectionUtils.isEmpty(needAddLabelList)) {
            labelService.saveBatch(needAddLabelList);
            List<LabelRef> labelRefList = needAddLabelList.stream()
                    .map(e -> {
                        LabelRef labelRef = new LabelRef();
                        labelRef.setUuid(UUID.randomUUID().toString());
                        // 0表示词库，1表示单词
                        labelRef.setRefType(0);
                        labelRef.setRefUuid(lexiconShowData.getUuid());
                        labelRef.setLabelUuid(e.getUuid());
                        labelRef.setCreateUserId(lexiconShowData.getCreateUserId());
                        return labelRef;
                    })
                    .collect(Collectors.toList());
            labelRefService.saveBatch(labelRefList);
        }
        if (!CollectionUtils.isEmpty(lexiconShowData.getDeleteTags())) {
            List<String> lableUUIDList = lexiconShowData.getDeleteTags()
                    .stream()
                    .map(Label::getUuid)
                    .collect(Collectors.toList());
            // 删除标签与词库的关联关系，标签可以不删除
            // 删除词库与标签的关联关系
            QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
            labelRefQueryWrapper.in("label_uuid", lableUUIDList).and(e -> e.eq("ref_uuid", lexiconShowData.getUuid()));
            labelRefService.remove(labelRefQueryWrapper);
        }
        return "修改成功";
    }

    @Override
    public List<Label> getLabelOfLexicon(String lexiconUUID) {
        // 词库的标签
        QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();
        labelRefQueryWrapper.eq("ref_uuid", lexiconUUID);
        List<LabelRef> labelRefList = labelRefService.list(labelRefQueryWrapper);
        Map<String, List<LabelRef>> labelRefGroupByLabelUUID = labelRefList.stream()
                .collect(Collectors.groupingBy(LabelRef::getLabelUuid));
        QueryWrapper<Label> labelQueryWrapper = new QueryWrapper<>();
        labelQueryWrapper.in("uuid", labelRefGroupByLabelUUID.keySet());
        return labelService.list(labelQueryWrapper);
    }


    private Map<String, List<Label>> getLabelNameList(List<Lexicon> lexicons) {
        List<String> lexiconUUIdList = Optional.ofNullable(lexicons)
                .orElse(Collections.emptyList())
                .stream()
                .map(Lexicon::getUuid)
                .collect(Collectors.toList());
        Assert.notEmpty(lexiconUUIdList, "请求参数错误");
        QueryWrapper<LabelRef> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("ref_uuid", lexiconUUIdList);

        Map<String, List<LabelRef>> labelRefMap = Optional.ofNullable(labelRefService.list(queryWrapper))
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.groupingBy(LabelRef::getRefUuid));

        Map<String, List<String>> labelUUIDMap = new HashMap<>();
        Optional.of(labelRefMap).ifPresent(e -> {
            e.forEach((k, v) -> {
                List<String> labelUUIDs = new ArrayList<>();
                v.forEach(labelRef -> {
                    labelUUIDs.add(labelRef.getLabelUuid());
                });
                labelUUIDMap.put(k, labelUUIDs);
            });
        });
        Map<String, List<Label>> result = new HashMap<>();
        if (!CollectionUtils.isEmpty(labelUUIDMap)) {
            QueryWrapper<Label> queryLabel = new QueryWrapper<>();
            List<String> labelUUIDList = new ArrayList<>();
            labelUUIDMap.values().forEach(labelUUIDList::addAll);
            queryLabel.in("uuid", labelUUIDList);
            List<Label> list = labelService.list(queryLabel);
            Optional.ofNullable(list)
                    .ifPresent(e -> {
                        Map<String, Label> labelMap = e.stream().collect(Collectors.toMap(Label::getUuid, Function.identity()));
                        labelUUIDMap.forEach((k, v) -> {
                            List<Label> labelNames = new ArrayList<>();
                            v.forEach(u -> {
                                if (labelMap.containsKey(u)) {
                                    labelNames.add(labelMap.get(u));
                                }
                            });
                            result.put(k, labelNames);
                        });
                    });
        }
        return result;
    }
}
