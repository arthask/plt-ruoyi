package com.example.pltool.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.FlashcardPackage;
import com.example.pltool.domain.entity.PackageCardRef;
import com.example.pltool.mapper.PackageCardRefMapper;
import com.example.pltool.service.PackageCardRefService;
import com.example.pltool.service.flashcard.FlashcardPackageService;

/**
 * <p>
 * 卡包闪卡关系表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@Service
public class PackageCardRefServiceImpl extends ServiceImpl<PackageCardRefMapper, PackageCardRef>
    implements PackageCardRefService {

  @Autowired
  private FlashcardPackageService flashcardPackageService;

  @Override
  public List<PackageInfoDto> getPackagesByCardUUId(String cardUUId) {
    QueryWrapper<PackageCardRef> cardRefQueryWrapper = new QueryWrapper<>();
    cardRefQueryWrapper.eq("card_uuid", cardUUId);
    List<PackageCardRef> packageCardRefList = list(cardRefQueryWrapper);
    if (CollectionUtils.isEmpty(packageCardRefList)) {
      return Collections.emptyList();
    }
    List<String> packageUUIdList = packageCardRefList.stream().map(PackageCardRef::getPackageUuid)
        .collect(Collectors.toList());
    QueryWrapper<FlashcardPackage> flashcardPackageQueryWrapper = new QueryWrapper<>();
    flashcardPackageQueryWrapper.in("uuid", packageUUIdList);
    List<FlashcardPackage> flashcardPackages =
        flashcardPackageService.list(flashcardPackageQueryWrapper);
    if (CollectionUtils.isEmpty(flashcardPackages)) {
      return Collections.emptyList();
    }
    List<PackageInfoDto> result = new ArrayList<>();
    flashcardPackages.forEach(p -> {
      PackageInfoDto packageInfoDto = new PackageInfoDto();
      packageInfoDto.setUuid(p.getUuid());
      packageInfoDto.setName(p.getName());
      packageInfoDto.setType(p.getType());
      result.add(packageInfoDto);
    });
    return result;
  }
}
