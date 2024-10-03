package com.example.filespringbootstarter.core.factory;


import com.example.filespringbootstarter.core.client.FileClient;
import com.example.filespringbootstarter.enums.FileStorageEnum;

public interface FileClientFactory {

    /**
     * 获取文件客户端
     * @param fileStorageEnum 存储类型
     * @return 文件客户端
     */
    FileClient getFileClient(FileStorageEnum fileStorageEnum);
}
