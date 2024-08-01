package com.example.pltool.controller.business.flashcard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pltool.domain.dto.flashcard.cardpackage.PackageInfoDto;
import com.example.pltool.domain.entity.FlashcardPackage;
import com.example.pltool.service.flashcard.FlashcardPackageService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 卡包表 前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-23
 */
@RestController
@RequestMapping("/flashcardPackage")
public class FlashcardPackageController extends BaseController {

    @Autowired
    private FlashcardPackageService flashcardPackageService;

    /**
     * 查询卡包列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(value = "name",required = false) String name) {
        startPage();
        QueryWrapper<FlashcardPackage> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("create_time");
        List<FlashcardPackage> list = flashcardPackageService.list(queryWrapper);
        return getDataTable(list);
    }

    @GetMapping("/getPackageInfo/{uuid}")
    public AjaxResult getPackageInfo(@PathVariable("uuid") String packageUUID){
        return AjaxResult.success(flashcardPackageService.getPackageInfo(packageUUID));
    }

    /**
     * 新增卡包
     */
    @PreAuthorize("@ss.hasPermi('system:flashcard:add')")
    @Log(title = "闪卡包", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PackageInfoDto packageInfoDto) {
        Long userId = getUserId();
        return AjaxResult.success(flashcardPackageService.addCardPackage(packageInfoDto, userId));
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody PackageInfoDto packageInfoDto) {
        Long userId = getUserId();
        return AjaxResult.success(flashcardPackageService.updateCardPackage(packageInfoDto, userId));
    }

    @DeleteMapping("/delete/{uuid}")
    public AjaxResult delete(@PathVariable("uuid") String uuid) {
        return AjaxResult.success(flashcardPackageService.removeByUUID(uuid));
    }

    /**
     * 获取卡包列表
     */
    @GetMapping("/getPackageList")
    public AjaxResult getPackageList(@RequestParam(value = "name", required = false) String name) {
        return AjaxResult.success(flashcardPackageService.getPackageList(name));
    }
}
