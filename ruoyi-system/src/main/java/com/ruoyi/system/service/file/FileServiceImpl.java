package com.ruoyi.system.service.file;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.fileclient.FileClient;
import com.ruoyi.common.utils.file.FileTypeUtils;
import com.ruoyi.common.utils.file.FileUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 文件 Service 实现类
 *
 * @author author
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;


    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content) {
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
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        return client.upload(content, path, type);
    }
//
//    @Override
//    public void deleteFile(Long id) throws Exception {
//        // 校验存在
//        FileDO file = validateFileExists(id);
//        // 从文件存储器中删除
//        FileClient client = fileConfigService.getFileClient(file.getConfigId());
//        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
//        client.delete(file.getPath());
//    }
//
//    private FileDO validateFileExists(Long id) {
//        FileDO fileDO = fileMapper.selectById(id);
//        if (fileDO == null) {
//            throw exception(FILE_NOT_EXISTS);
//        }
//        return fileDO;
//    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        return client.getContent(path);
    }

}
