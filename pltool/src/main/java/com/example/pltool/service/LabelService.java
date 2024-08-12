package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.Label;

import java.util.List;


/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
public interface LabelService extends IService<Label> {

    /**
     * 使用uuid获取标签信息
     * @param uuid uuid
     * @return
     */
    Label getLabelByUUID(String uuid);

    /**
     * 判断标签名是否重复
     * @param name
     * @return
     */
    boolean isDuplicateName(String name);

    /**
     * 获取所有的标签
     * @return
     */
    List<LabelInfo> getAllLabels(Long userId);

    /**
     * 创建标签
     *
     * @param labelInfo
     * @return
     */
    boolean createLabel(LabelInfo labelInfo);

    /**
     * 更新标签,修改标签名称
     *
     * @param labelInfo
     * @return
     */
    boolean updateLabel(LabelInfo labelInfo);

    /**
     * 获取标签信息
     *
     * @param uuid
     * @return
     */
    LabelInfo getLabelInfo(String uuid);

}
