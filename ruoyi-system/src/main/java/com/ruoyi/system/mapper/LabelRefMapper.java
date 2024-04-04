package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LabelRef;

/**
 * 标签关联Mapper接口
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public interface LabelRefMapper 
{
    /**
     * 查询标签关联
     * 
     * @param id 标签关联主键
     * @return 标签关联
     */
    public LabelRef selectLabelRefById(Long id);

    /**
     * 查询标签关联列表
     * 
     * @param labelRef 标签关联
     * @return 标签关联集合
     */
    public List<LabelRef> selectLabelRefList(LabelRef labelRef);

    /**
     * 新增标签关联
     * 
     * @param labelRef 标签关联
     * @return 结果
     */
    public int insertLabelRef(LabelRef labelRef);

    /**
     * 修改标签关联
     * 
     * @param labelRef 标签关联
     * @return 结果
     */
    public int updateLabelRef(LabelRef labelRef);

    /**
     * 删除标签关联
     * 
     * @param id 标签关联主键
     * @return 结果
     */
    public int deleteLabelRefById(Long id);

    /**
     * 批量删除标签关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLabelRefByIds(Long[] ids);
}
