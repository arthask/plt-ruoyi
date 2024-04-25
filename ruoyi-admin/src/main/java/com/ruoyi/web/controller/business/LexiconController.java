package com.ruoyi.web.controller.business;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.system.domain.dto.LexiconData;
import com.ruoyi.system.domain.dto.LexiconShowData;
import com.ruoyi.system.gencode.entity.Lexicon;
import com.ruoyi.system.gencode.service.LexiconService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 词库Controller
 *
 * @author ruoyi
 * @date 2024-04-04
 */
@RestController
@RequestMapping("/system/lexicon")
public class LexiconController extends BaseController {
    @Autowired
    private LexiconService lexiconService;

    /**
     * 查询词库列表
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:list')")
    @GetMapping("/list")
    public TableDataInfo list(Lexicon lexicon) {
        startPage();
        Wrapper<Lexicon> queryWrapper = new QueryWrapper<>(lexicon);
        List<Lexicon> list = lexiconService.list(queryWrapper);
        List<LexiconShowData> lexiconShowData = lexiconService.convertLexiconList(list);
        return getDataTable(lexiconShowData);
    }

    /**
     * 导出词库列表
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:export')")
    @Log(title = "词库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Lexicon lexicon) {
        List<Lexicon> list = new ArrayList<>();
        ExcelUtil<Lexicon> util = new ExcelUtil<Lexicon>(Lexicon.class);
        util.exportExcel(response, list, "词库数据");
    }

    /**
     * 获取词库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(lexiconService.getInfo(id));
    }

    /**
     * 新增词库
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:add')")
    @Log(title = "词库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestParam("file") MultipartFile file,
                          @RequestParam("name") String name,
                          @RequestParam("language") String language,
                          @RequestParam("label") List<String> labelList) {
        Long userId = getUserId();
        LexiconData lexicon = new LexiconData();
        lexicon.setFile(file);
        lexicon.setName(name);
        lexicon.setLanguage(language);
        lexicon.setUserId(userId);
        lexicon.setLabelList(labelList);
        return AjaxResult.success(lexiconService.createLexicon(lexicon));
    }

    /**
     * 修改词库
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:edit')")
    @Log(title = "词库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LexiconShowData lexicon) {
        lexicon.setCreateUserId(this.getUserId());
        return AjaxResult.success(lexiconService.updateLexicon(lexicon));
    }

    /**
     * 删除词库
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:remove')")
    @Log(title = "词库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return AjaxResult.success(lexiconService.removeLexicon(ids));
    }
}
