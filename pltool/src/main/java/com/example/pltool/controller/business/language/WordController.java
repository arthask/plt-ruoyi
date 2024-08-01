package com.example.pltool.controller.business.language;

import com.example.pltool.domain.entity.Word;
import com.example.pltool.service.language.WordService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 单词Controller
 *
 * @author ruoyi
 * @date 2023-12-28
 */
@RestController
@RequestMapping("/system/word")
public class WordController extends BaseController {
    @Resource(name = "wordService")
    private WordService newWordService;


    /**
     * 查询单词列表
     */
    @PreAuthorize("@ss.hasPermi('system:word:list')")
    @GetMapping("/list")
    public TableDataInfo list(Word word) {
        startPage();
        List<Word> list = newWordService.list();
        return getDataTable(list);
    }

    /**
     * 导出单词列表
     */
    @PreAuthorize("@ss.hasPermi('system:word:export')")
    @Log(title = "单词", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Word word) {

    }

    /**
     * 获取单词详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:word:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(newWordService.getById(id));
    }

    /**
     * 新增单词
     */
    @PreAuthorize("@ss.hasPermi('system:word:add')")
    @Log(title = "单词", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Word word) {
        return toAjax(newWordService.save(word));
    }

    /**
     * 修改单词
     */
    @PreAuthorize("@ss.hasPermi('system:word:edit')")
    @Log(title = "单词", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Word word) {
        return toAjax(newWordService.updateById(word));
    }

    /**
     * 删除单词
     */
    @PreAuthorize("@ss.hasPermi('system:word:remove')")
    @Log(title = "单词", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(newWordService.removeBatchByIds(Arrays.asList(ids)));
    }

    @Log(title = "单词", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Word> util = new ExcelUtil<>(Word.class);
        List<Word> wordList = util.importExcel(file.getInputStream());
        String message = newWordService.importWords(wordList, updateSupport, this.getUserId());
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<Word> util = new ExcelUtil<>(Word.class);
        return util.importTemplateExcel("单词数据");
    }

    @GetMapping("/getOneWord/{lexiconUUId}/{index}")
    public AjaxResult getOneWord(@PathVariable("lexiconUUId") String lexiconUUId, @PathVariable("index") int index) {
        return success(newWordService.getOneWord(this.getUserId(), lexiconUUId, index));
    }

    /**
     * 从内置词库中查找单词
     *
     * @param searchCn
     * @return
     */
    @GetMapping("/searchWordByCN")
    public AjaxResult searchWordByCN(@RequestParam("searchCn") String searchCn) {
        return success(newWordService.searchWordByCN(searchCn));
    }

    @GetMapping("/searchWord")
    public AjaxResult searchWord(@RequestParam("word") String word) {
        return success(newWordService.searchWord(word));
    }

    /**
     * 从内置词库中查找单词
     *
     * @param wordUuid
     * @return
     */
    @GetMapping("/getWordInfo")
    public AjaxResult getWordInfo(@RequestParam("wordUuid") String wordUuid) {
        return success(newWordService.getWordInfo(wordUuid));
    }
}
