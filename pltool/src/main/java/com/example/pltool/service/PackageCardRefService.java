package com.example.pltool.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.PackageCardRef;

import java.util.List;

/**
 * <p>
 * 卡包闪卡关系表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
public interface PackageCardRefService extends IService<PackageCardRef> {
    List<PackageInfoDto> getPackagesByCardUUId(String cardUUId);

}
