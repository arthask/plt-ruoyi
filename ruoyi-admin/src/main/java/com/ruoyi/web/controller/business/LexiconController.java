package com.ruoyi.web.controller.business;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.uuid.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Lexicon;
import com.ruoyi.system.service.ILexiconService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

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
    private ILexiconService lexiconService;

    /**
     * 查询词库列表
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:list')")
    @GetMapping("/list")
    public TableDataInfo list(Lexicon lexicon) {
        startPage();
        List<Lexicon> list = lexiconService.selectLexiconList(lexicon);
        return getDataTable(list);
    }

    /**
     * 导出词库列表
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:export')")
    @Log(title = "词库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Lexicon lexicon) {
        List<Lexicon> list = lexiconService.selectLexiconList(lexicon);
        ExcelUtil<Lexicon> util = new ExcelUtil<Lexicon>(Lexicon.class);
        util.exportExcel(response, list, "词库数据");
    }

    /**
     * 获取词库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(lexiconService.selectLexiconById(id));
    }

    /**
     * 新增词库
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:add')")
    @Log(title = "词库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Lexicon lexicon) {
        Long userId = getUserId();
        lexicon.setCreateUserId(userId);
        lexicon.setUuid(UUID.randomUUID().toString());
        return toAjax(lexiconService.insertLexicon(lexicon));
    }

    /**
     * 修改词库
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:edit')")
    @Log(title = "词库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Lexicon lexicon) {
        return toAjax(lexiconService.updateLexicon(lexicon));
    }

    /**
     * 删除词库
     */
    @PreAuthorize("@ss.hasPermi('system:lexicon:remove')")
    @Log(title = "词库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(lexiconService.deleteLexiconByIds(ids));
    }
}
