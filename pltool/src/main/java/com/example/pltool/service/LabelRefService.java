package com.example.pltool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pltool.domain.dto.label.LabelInfo;
import com.example.pltool.domain.entity.LabelRef;


import java.util.List;

/**
 * <p>
 * 标签关联表 服务类
 * </p>
 *
 * @author author
 * @since 2024-04-12
 */
public interface LabelRefService extends IService<LabelRef> {

    /**
     * 使用关联uuid获取标签信息
     * @param refUUID 关联uuid
     * @return
     */
    List<LabelInfo> getLabelInfoByRefUUID(String refUUID);

}
