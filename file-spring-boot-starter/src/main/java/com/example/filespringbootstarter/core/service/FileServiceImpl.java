package com.example.filespringbootstarter.core.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.example.filespringbootstarter.core.client.FileClient;
import com.example.filespringbootstarter.core.factory.FileClientFactory;
import com.example.filespringbootstarter.enums.FileStorageEnum;
import com.example.filespringbootstarter.utils.file.FileTypeUtils;
import com.example.filespringbootstarter.utils.file.FileUtils;
import lombok.SneakyThrows;



/**
 * 文件 Service 实现类
 *
 * @author author
 */
public class FileServiceImpl implements FileService {

    private FileClientFactory fileClientFactory;

    public FileServiceImpl(FileClientFactory fileClientFactory) {
        this.fileClientFactory = fileClientFactory;
    }

    @Override
    @SneakyThrows
    public String createFile(FileStorageEnum fileStorageEnum, String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileClientFactory.getFileClient(fileStorageEnum);
        Assert.notNull(client, "客户端(master) 不能为空");
        return client.upload(content, path, type);
    }

    @Override
    public void deleteFile(FileStorageEnum fileStorageEnum, String path) throws Exception {
        // 从文件存储器中删除
        FileClient client = fileClientFactory.getFileClient(fileStorageEnum);
        Assert.notNull(client, "客户端({}) 不能为空", fileStorageEnum);
        client.delete(path);
    }

    @Override
    public byte[] getFileContent(FileStorageEnum fileStorageEnum, String path) throws Exception {
        FileClient client = fileClientFactory.getFileClient(fileStorageEnum);
        Assert.notNull(client, "客户端({}) 不能为空", fileStorageEnum);
        return client.getContent(path);
    }

}
