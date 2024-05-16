package com.ruoyi.system.gencode.service.impl;

import cn.hutool.core.lang.UUID;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.gencode.entity.UserStudyRecord;
import com.ruoyi.system.gencode.mapper.UserStudyRecordMapper;
import com.ruoyi.system.gencode.service.UserStudyRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * <p>
 * 用户学习记录表 服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-16
 */
@Service
public class UserStudyRecordServiceImpl extends ServiceImpl<UserStudyRecordMapper, UserStudyRecord> implements UserStudyRecordService {
    @Override
    public int insertUserStudyRecord(UserStudyRecord userStudyRecord) {
        userStudyRecord.setUuid(UUID.randomUUID().toString().replace("-", ""));
        userStudyRecord.setStudyTime(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
        userStudyRecord.setCreateTime(LocalDateTime.ofInstant(DateUtils.getNowDate().toInstant(),ZoneId.systemDefault()));
        return getBaseMapper().insert(userStudyRecord);
    }
}
