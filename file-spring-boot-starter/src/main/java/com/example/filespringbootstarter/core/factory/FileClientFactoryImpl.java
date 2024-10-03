package com.example.filespringbootstarter.core.factory;

import cn.hutool.core.util.ReflectUtil;
import com.example.filespringbootstarter.config.FileClientConfig;
import com.example.filespringbootstarter.core.client.AbstractFileClient;
import com.example.filespringbootstarter.core.client.FileClient;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 文件客户端的工厂实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
public class FileClientFactoryImpl implements FileClientFactory {

    @Resource(name = "localFileClientConfig")
    private FileClientConfig localFileClientConfig;

    @Resource(name = "s3FileClientConfig")
    private FileClientConfig s3FileClientConfig;

    /**
     * 文件客户端 Map
     * key：配置编号
     */
    private final ConcurrentMap<FileStorageEnum, FileClient> clients = new ConcurrentHashMap<FileStorageEnum, FileClient>() {{
        put(FileStorageEnum.LOCAL, createFileClient(FileStorageEnum.LOCAL, localFileClientConfig));
        put(FileStorageEnum.S3, createFileClient(FileStorageEnum.S3, s3FileClientConfig));
    }};


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
