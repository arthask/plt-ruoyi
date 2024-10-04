package com.example.filespringbootstarter.core.factory;

import cn.hutool.core.util.ReflectUtil;
import com.example.filespringbootstarter.config.FileClientConfig;
import com.example.filespringbootstarter.core.client.AbstractFileClient;
import com.example.filespringbootstarter.core.client.FileClient;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 文件客户端的工厂实现类
 *
 * @author 芋道源码
 */
@Slf4j
public class FileClientFactoryImpl implements FileClientFactory {

    /**
     * 文件客户端 Map
     * key：配置编号
     */
    private final ConcurrentMap<FileStorageEnum, FileClient> clients = new ConcurrentHashMap<>();

    public FileClientFactoryImpl(FileClientConfig localFileClientConfig, FileClientConfig s3FileClientConfig) {
        clients.put(FileStorageEnum.LOCAL, createFileClient(FileStorageEnum.LOCAL, localFileClientConfig));
        clients.put(FileStorageEnum.S3, createFileClient(FileStorageEnum.S3, s3FileClientConfig));
    }

    @Override
    public FileClient getFileClient(FileStorageEnum fileStorageEnum) {
        fileStorageEnum = Optional.ofNullable(fileStorageEnum).orElse(FileStorageEnum.LOCAL);
        return clients.get(fileStorageEnum);
    }


    private <Config extends FileClientConfig> AbstractFileClient<Config> createFileClient(FileStorageEnum fileStorageEnum, FileClientConfig fileClientConfig) {
        // 创建客户端
        return (AbstractFileClient<Config>) ReflectUtil.newInstance(fileStorageEnum.getClientClass(), fileClientConfig);
    }

}
