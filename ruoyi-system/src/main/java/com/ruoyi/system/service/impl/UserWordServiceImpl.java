package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserWord;
import com.ruoyi.system.mapper.OldUserWordMapper;
import com.ruoyi.system.service.IUserWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户单词Service业务层处理
 *
 * @author ruoyi
 * @date 2024-01-06
 */
@Service("oldUserWordService")
public class UserWordServiceImpl implements IUserWordService {
    @Autowired
    private OldUserWordMapper oldUserWordMapper;

    /**
     * 查询用户单词
     *
     * @param id 用户单词主键
     * @return 用户单词
     */
    @Override
    public UserWord selectUserWordById(Long id) {
        return oldUserWordMapper.selectUserWordById(id);
    }

    /**
     * 查询用户单词列表
     *
     * @param userWord 用户单词
     * @return 用户单词
     */
    @Override
    public List<UserWord> selectUserWordList(UserWord userWord) {
        return oldUserWordMapper.selectUserWordList(userWord);
    }

    /**
     * 新增用户单词
     *
     * @param userWord 用户单词
     * @return 结果
     */
    @Deprecated
    @Override
    public int insertUserWord(UserWord userWord) {
        return 0;
    }


    /**
     * 修改用户单词
     *
     * @param userWord 用户单词
     * @return 结果
     */
    @Override
    public int updateUserWord(UserWord userWord) {
        userWord.setUpdateTime(DateUtils.getNowDate());
        return oldUserWordMapper.updateUserWord(userWord);
    }

    /**
     * 批量删除用户单词
     *
     * @param ids 需要删除的用户单词主键
     * @return 结果
     */
    @Override
    public int deleteUserWordByIds(Long[] ids) {
        return oldUserWordMapper.deleteUserWordByIds(ids);
    }

    /**
     * 删除用户单词信息
     *
     * @param id 用户单词主键
     * @return 结果
     */
    @Override
    public int deleteUserWordById(Long id) {
        return oldUserWordMapper.deleteUserWordById(id);
    }

}
