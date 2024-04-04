package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Label;

/**
 * 标签Service接口
 * 
 * @author ruoyi
 * @date 2024-04-04
 */
public interface ILabelService 
{
    /**
     * 查询标签
     * 
     * @param id 标签主键
     * @return 标签
     */
    public Label selectLabelById(Long id);

    /**
     * 查询标签列表
     * 
     * @param label 标签
     * @return 标签集合
     */
    public List<Label> selectLabelList(Label label);

    /**
     * 新增标签
     * 
     * @param label 标签
     * @return 结果
     */
    public int insertLabel(Label label);

    /**
     * 修改标签
     * 
     * @param label 标签
     * @return 结果
     */
    public int updateLabel(Label label);

    /**
     * 批量删除标签
     * 
     * @param ids 需要删除的标签主键集合
     * @return 结果
     */
    public int deleteLabelByIds(Long[] ids);

    /**
     * 删除标签信息
     * 
     * @param id 标签主键
     * @return 结果
     */
    public int deleteLabelById(Long id);
}
