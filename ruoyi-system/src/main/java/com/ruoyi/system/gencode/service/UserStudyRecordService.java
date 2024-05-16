package com.ruoyi.system.gencode.service;

import com.ruoyi.system.gencode.entity.UserStudyRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户学习记录表 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
public interface UserStudyRecordService extends IService<UserStudyRecord> {
    int insertUserStudyRecord(UserStudyRecord userStudyRecord);
}
