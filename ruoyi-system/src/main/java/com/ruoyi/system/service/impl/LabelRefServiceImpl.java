package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LabelRefMapper;
import com.ruoyi.system.domain.LabelRef;
import com.ruoyi.system.service.ILabelRefService;

/**
 * 标签关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
@Service
public class LabelRefServiceImpl implements ILabelRefService 
{
    @Autowired
    private LabelRefMapper labelRefMapper;

    /**
     * 查询标签关联
     * 
     * @param id 标签关联主键
     * @return 标签关联
     */
    @Override
    public LabelRef selectLabelRefById(Long id)
    {
        return labelRefMapper.selectLabelRefById(id);
    }

    /**
     * 查询标签关联列表
     * 
     * @param labelRef 标签关联
     * @return 标签关联
     */
    @Override
    public List<LabelRef> selectLabelRefList(LabelRef labelRef)
    {
        return labelRefMapper.selectLabelRefList(labelRef);
    }

    /**
     * 新增标签关联
     * 
     * @param labelRef 标签关联
     * @return 结果
     */
    @Override
    public int insertLabelRef(LabelRef labelRef)
    {
        labelRef.setCreateTime(DateUtils.getNowDate());
        return labelRefMapper.insertLabelRef(labelRef);
    }

    /**
     * 修改标签关联
     * 
     * @param labelRef 标签关联
     * @return 结果
     */
    @Override
    public int updateLabelRef(LabelRef labelRef)
    {
        labelRef.setUpdateTime(DateUtils.getNowDate());
        return labelRefMapper.updateLabelRef(labelRef);
    }

    /**
     * 批量删除标签关联
     * 
     * @param ids 需要删除的标签关联主键
     * @return 结果
     */
    @Override
    public int deleteLabelRefByIds(Long[] ids)
    {
        return labelRefMapper.deleteLabelRefByIds(ids);
    }

    /**
     * 删除标签关联信息
     * 
     * @param id 标签关联主键
     * @return 结果
     */
    @Override
    public int deleteLabelRefById(Long id)
    {
        return labelRefMapper.deleteLabelRefById(id);
    }
}
