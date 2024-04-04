package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LabelMapper;
import com.ruoyi.system.domain.Label;
import com.ruoyi.system.service.ILabelService;

/**
 * 标签Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@Service
public class LabelServiceImpl implements ILabelService 
{
    @Autowired
    private LabelMapper labelMapper;

    /**
     * 查询标签
     * 
     * @param id 标签主键
     * @return 标签
     */
    @Override
    public Label selectLabelById(Long id)
    {
        return labelMapper.selectLabelById(id);
    }

    /**
     * 查询标签列表
     * 
     * @param label 标签
     * @return 标签
     */
    @Override
    public List<Label> selectLabelList(Label label)
    {
        return labelMapper.selectLabelList(label);
    }

    /**
     * 新增标签
     * 
     * @param label 标签
     * @return 结果
     */
    @Override
    public int insertLabel(Label label)
    {
        label.setCreateTime(DateUtils.getNowDate());
        return labelMapper.insertLabel(label);
    }

    /**
     * 修改标签
     * 
     * @param label 标签
     * @return 结果
     */
    @Override
    public int updateLabel(Label label)
    {
        label.setUpdateTime(DateUtils.getNowDate());
        return labelMapper.updateLabel(label);
    }

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的标签主键
     * @return 结果
     */
    @Override
    public int deleteLabelByIds(Long[] ids)
    {
        return labelMapper.deleteLabelByIds(ids);
    }

    /**
     * 删除标签信息
     * 
     * @param id 标签主键
     * @return 结果
     */
    @Override
    public int deleteLabelById(Long id)
    {
        return labelMapper.deleteLabelById(id);
    }
}
