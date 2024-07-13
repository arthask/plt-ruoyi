package com.example.pltool.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.FlashcardPackage;
import com.example.pltool.domain.entity.Label;
import com.example.pltool.domain.entity.LabelRef;
import com.example.pltool.mapper.FlashcardPackageMapper;
import com.example.pltool.service.FlashcardPackageService;
import com.example.pltool.service.LabelRefService;
import com.example.pltool.service.LabelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 卡包表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
@Service
public class FlashcardPackageServiceImpl extends ServiceImpl<FlashcardPackageMapper, FlashcardPackage> implements FlashcardPackageService {

    @Autowired
    private LabelRefService labelRefService;

    @Autowired
    private LabelService labelService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addCardPackage(PackageInfoDto packageInfoDto, Long userId) {
        FlashcardPackage flashcardPackage = new FlashcardPackage();
        String packageUUID = UUID.randomUUID().toString().replace("-", "");
        flashcardPackage.setUuid(packageUUID);
        flashcardPackage.setName(packageInfoDto.getName());
        flashcardPackage.setType(packageInfoDto.getType());
        flashcardPackage.setUserId(userId);
        save(flashcardPackage);
        if (!CollectionUtils.isEmpty(packageInfoDto.getLabelInfos())) {
            List<LabelRef> labelRefs = new ArrayList<>();
            List<Label> labels = new ArrayList<>();
            for (LabelInfo labelInfo : packageInfoDto.getLabelInfos()) {
                // 新增标签
                Label label = new Label();
                label.setName(labelInfo.getName());
                String labelUUID = UUID.randomUUID().toString().replace("-", "");
                label.setUuid(labelUUID);
                label.setCreateUserId(userId);
                labels.add(label);
                // 新增卡包和标签关联关系
                LabelRef labelRef = new LabelRef();
                labelRef.setLabelUuid(labelUUID);
                labelRef.setRefUuid(packageUUID);
                labelRef.setCreateUserId(userId);
                labelRef.setRefType(2);
                labelRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
                labelRefs.add(labelRef);
            }
            labelRefService.saveBatch(labelRefs);
            labelService.saveBatch(labels);
        }
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateCardPackage(PackageInfoDto packageInfoDto, Long userId) {
        // 卡片类型不能修改
        FlashcardPackage updatePackage = new FlashcardPackage();
        updatePackage.setUpdateTime(LocalDateTime.now());
        updatePackage.setName(packageInfoDto.getName());
        QueryWrapper<FlashcardPackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", packageInfoDto.getUuid());
        update(updatePackage, queryWrapper);
        // 分标签的新增和删除
        Map<String, String> labelMap = packageInfoDto.getLabelInfos()
                .stream()
                .collect(Collectors.toMap(LabelInfo::getUuid, LabelInfo::getName, (k1, k2) -> k1));
        List<Label> needAddLabelList = new ArrayList<>();
        List<LabelRef> needAddRefList = new ArrayList<>();
        AtomicInteger deleteCount = new AtomicInteger();
        if (!CollectionUtils.isEmpty(labelMap.keySet())) {
            QueryWrapper<Label> labelQuery = new QueryWrapper<>();
            labelQuery.in("uuid", labelMap.keySet());
            List<Label> labelFromDb = labelService.list(labelQuery);
            Map<String, String> labelMapFromDb = labelFromDb
                    .stream()
                    .collect(Collectors.toMap(Label::getUuid, Label::getName, (k1, k2) -> k1));
            QueryWrapper<LabelRef> labelRefQueryWrapper = new QueryWrapper<>();

            labelMap.forEach((k, v) -> {
                if (!labelMapFromDb.containsKey(k)) {
                    String labelUUID = UUID.randomUUID().toString().replace("-", "");
                    Label addLabel = new Label();
                    addLabel.setName(v);
                    addLabel.setUuid(labelUUID);
                    addLabel.setCreateUserId(userId);
                    needAddLabelList.add(addLabel);
                    LabelRef addRef = new LabelRef();
                    addRef.setRefUuid(packageInfoDto.getUuid());
                    addRef.setLabelUuid(labelUUID);
                    addRef.setRefType(2);
                    addRef.setUuid(UUID.randomUUID().toString().replace("-", ""));
                    addRef.setCreateUserId(userId);
                    needAddRefList.add(addRef);
                } else {
                    deleteCount.getAndIncrement();
                    // 将卡包关联的标签移除
                    labelRefQueryWrapper.eq("ref_uuid", packageInfoDto.getUuid())
                            .eq("label_uuid", k).or();
                }
            });
            if (!CollectionUtils.isEmpty(needAddLabelList)) {
                labelService.saveBatch(needAddLabelList);
            }
            if (!CollectionUtils.isEmpty(needAddRefList)) {
                labelRefService.saveBatch(needAddRefList);
            }
            if (deleteCount.get() > 0) {
                labelRefQueryWrapper.eq("ref_type", 2);
                labelRefService.remove(labelRefQueryWrapper);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public List<Map<String, String>> getPackageList(String name) {
        QueryWrapper<FlashcardPackage> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        List<FlashcardPackage> flashcardPackages = list(queryWrapper);
        if (CollectionUtils.isEmpty(flashcardPackages)) {
            return Collections.emptyList();
        }
        List<Map<String, String>> result = new ArrayList<>();
        flashcardPackages
                .forEach(e -> {
                    Map<String, String> entity = new HashMap<>();
                    entity.put("uuid", e.getUuid());
                    entity.put("name", e.getName());
                    result.add(entity);
                });
        return result;
    }

    @Override
    public PackageInfoDto getPackageInfo(String packageUUID) {
        QueryWrapper<FlashcardPackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", packageUUID);
        FlashcardPackage flashcardPackage = getOne(queryWrapper);
        PackageInfoDto packageInfoDto = new PackageInfoDto();
        BeanUtils.copyProperties(flashcardPackage, packageInfoDto);
        // 查询标签
        List<LabelInfo> labelInfoByRefUUID = labelRefService.getLabelInfoByRefUUID(packageUUID);
        if (!CollectionUtils.isEmpty(labelInfoByRefUUID)) {
            packageInfoDto.setLabelInfos(labelInfoByRefUUID);
        }
        return packageInfoDto;
    }

    @Override
    public Boolean removeByUUID(String uuid) {
        Assert.notNull(uuid, "请求参数错误");
        QueryWrapper<FlashcardPackage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uuid", uuid);
        return remove(queryWrapper);
    }
}
