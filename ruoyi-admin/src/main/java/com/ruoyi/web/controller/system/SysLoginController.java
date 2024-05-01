package com.ruoyi.web.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.enums.SnapshotType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.domain.Word;
import com.ruoyi.system.domain.WordSnapshot;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.IWordService;
import com.ruoyi.system.service.IWordSnapshotService;
import com.ruoyi.system.service.review.IWordReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private IWordReviewService wordReviewService;

    @Autowired
    private IWordSnapshotService wordSnapshotService;

    @Autowired
    private IWordService wordService;

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     * 
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        // 生成快照
//        Long userId = SecurityUtils.getUserId();
//        wordSnapshotService.deleteByUserIdAndType(userId, SnapshotType.REVIEW.getValue());
//        List<Word> needReviewWords = wordReviewService.getNeedReviewWords(userId);
//        // 批量需复习插入
//        batchSaveWordSnapshot(needReviewWords, userId, SnapshotType.REVIEW.getValue());
//
//        wordSnapshotService.deleteByUserIdAndType(userId, SnapshotType.NEW.getValue());
//        List<Word> newWords = wordService.getNewWordOfUser(userId);
//        // 批量新单词插入
//        batchSaveWordSnapshot(newWords, userId, SnapshotType.NEW.getValue());
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    private void batchSaveWordSnapshot(List<Word> needReviewWords, Long userId, int type) {
        List<WordSnapshot> wordSnapshots = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(needReviewWords)) {
            needReviewWords.forEach(e -> {
                WordSnapshot wordSnapshot = new WordSnapshot();
                wordSnapshot.setUserId(userId);
                wordSnapshot.setWordId(e.getId());
                wordSnapshot.setType(type);
                wordSnapshots.add(wordSnapshot);
            });
            wordSnapshotService.insertBatch(wordSnapshots);
        }
    }
}
