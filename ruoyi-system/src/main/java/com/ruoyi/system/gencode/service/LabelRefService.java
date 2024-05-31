package com.ruoyi.system.gencode.service;

import com.ruoyi.system.domain.dto.label.LabelInfo;
import com.ruoyi.system.gencode.entity.LabelRef;
import com.baomidou.mybatisplus.extension.service.IService;

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
