package com.example.pltool.service.flashcard;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.FlashcardPackage;


import java.util.List;
import java.util.Map;

/**
 * <p>
 * 卡包表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-29
 */
public interface FlashcardPackageService extends IService<FlashcardPackage> {
    /**
     * 添加卡包
     * @param packageInfoDto
     * @param userId
     * @return
     */
    Boolean addCardPackage(PackageInfoDto packageInfoDto, Long userId);

    /**
     * 添加卡包
     * @param packageInfoDto
     * @return
     */
    Boolean updateCardPackage(PackageInfoDto packageInfoDto, Long userId);

    /**
     * 删除卡包
     * @param uuid
     * @return
     */
    Boolean removeByUUID(String uuid);

    /**
     * 获取卡包列表
     * @return
     */
    List<Map<String,String>> getPackageList(String name);

    /**
     * 获取卡包详情
     * @param packageUUID
     * @return
     */
    PackageInfoDto getPackageInfo(String packageUUID);
}
