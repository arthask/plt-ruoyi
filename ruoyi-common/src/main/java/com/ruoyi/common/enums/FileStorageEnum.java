package com.ruoyi.common.enums;

import cn.hutool.core.util.ArrayUtil;
import com.ruoyi.common.core.fileclient.FileClient;
import com.ruoyi.common.core.fileclient.FileClientConfig;
import com.ruoyi.common.core.fileclient.local.LocalFileClient;
import com.ruoyi.common.core.fileclient.local.LocalFileClientConfig;
import com.ruoyi.common.core.fileclient.s3.S3FileClient;
import com.ruoyi.common.core.fileclient.s3.S3FileClientConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileStorageEnum {
    LOCAL(10, LocalFileClientConfig.class, LocalFileClient.class),

    S3(20, S3FileClientConfig.class, S3FileClient.class),
    ;

    /**
     * 存储器
     */
    private final Integer storage;

    /**
     * 配置类
     */
    private final Class<? extends FileClientConfig> configClass;
    /**
     * 客户端类
     */
    private final Class<? extends FileClient> clientClass;

    public static FileStorageEnum getByStorage(Integer storage) {
        return ArrayUtil.firstMatch(o -> o.getStorage().equals(storage), values());
    }
}
