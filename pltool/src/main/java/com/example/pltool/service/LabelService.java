package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.entity.Label;


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

}
