package com.example.pltool.controller.business.language;

import com.example.pltool.domain.entity.UserWord;
import com.example.pltool.service.UserWordService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户单词Controller
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@RestController
@RequestMapping("/system/userword")
public class UserWordController extends BaseController {

    @Autowired
    private UserWordService newUserWordService;

    /**
     * 查询用户单词列表
     */
    @PreAuthorize("@ss.hasPermi('system:word:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserWord userWord) {
        startPage();
//        QueryWrapper<UserWord> queryWrapper = new QueryWrapper<>();
        List<UserWord> list = newUserWordService.list();
        return getDataTable(list);
    }

    /**
     * 获取用户单词详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:word:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(newUserWordService.getById(id));
    }

    /**
     * 新增用户单词
     */
    @PreAuthorize("@ss.hasPermi('system:word:add')")
    @Log(title = "用户单词", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserWord userWord) {
        userWord.setUserName(getUsername());
        userWord.setUserId(getUserId());
        return toAjax(newUserWordService.insertUserWord(userWord));
    }

    /**
     * 修改用户单词
     */
    @PreAuthorize("@ss.hasPermi('system:word:edit')")
    @Log(title = "用户单词", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserWord userWord) {
        return toAjax(newUserWordService.updateById(userWord));
    }

    /**
     * 删除用户单词
     */
    @PreAuthorize("@ss.hasPermi('system:word:remove')")
    @Log(title = "用户单词", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(newUserWordService.removeBatchByIds(Arrays.asList(ids)));
    }


    @PostMapping("/collect")
    public AjaxResult collect(@RequestParam("wordUUID") String wordUUID) {
        return success(newUserWordService.collect(wordUUID, getUserId(), getUsername()));
    }
}
