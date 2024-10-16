package com.example.filespringbootstarter.utils.file;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.example.filespringbootstarter.utils.date.DateUtils;
import com.example.filespringbootstarter.utils.strings.StringUtils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.SneakyThrows;

/**
 * 文件工具类
 *
 * @author 芋道源码
 */
public class FileUtils {

  /**
   * 创建临时文件 该文件会在 JVM 退出时，进行删除
   *
   * @param data 文件内容
   * @return 文件
   */
  @SneakyThrows
  public static File createTempFile(String data) {
    File file = createTempFile();
    // 写入内容
    FileUtil.writeUtf8String(data, file);
    return file;
  }

  /**
   * 创建临时文件 该文件会在 JVM 退出时，进行删除
   *
   * @param data 文件内容
   * @return 文件
   */
  @SneakyThrows
  public static File createTempFile(byte[] data) {
    File file = createTempFile();
    // 写入内容
    FileUtil.writeBytes(data, file);
    return file;
  }

  /**
   * 创建临时文件，无内容 该文件会在 JVM 退出时，进行删除
   *
   * @return 文件
   */
  @SneakyThrows
  public static File createTempFile() {
    // 创建文件，通过 UUID 保证唯一
    File file = File.createTempFile(IdUtil.simpleUUID(), null);
    // 标记 JVM 退出时，自动删除
    file.deleteOnExit();
    return file;
  }

  /**
   * 生成文件路径
   *
   * @param content 文件内容
   * @param originalName 原始文件名
   * @return path，唯一不可重复
   */
  public static String generatePath(byte[] content, String originalName) {
    String sha256Hex = DigestUtil.sha256Hex(content);
    // 情况一：如果存在 name，则优先使用 name 的后缀
    if (StrUtil.isNotBlank(originalName)) {
      String extName = FileNameUtil.extName(originalName);
      return StrUtil.isBlank(extName) ? sha256Hex : sha256Hex + "." + extName;
    }
    // 情况二：基于 content 计算
    return sha256Hex + '.' + FileTypeUtil.getType(new ByteArrayInputStream(content));
  }


  /**
   * 获取文件扩展名
   *
   * @param file MultipartFile
   * @return 文件扩展名，不带。
   */
  public static String getWebFileExtName(MultipartFile file) {
    // 情况一：如果存在 name，则优先使用 name 的后缀
    String originalFilename = file.getOriginalFilename();
    Assert.notNull(originalFilename, "文件名称为空，无法识别文件类型");
    return FileNameUtil.extName(originalFilename);
  }

  /**
   * 编码文件名
   */
  public static String extractUploadWebFilePath(MultipartFile file) {
    return StringUtils.format("{}/{}.{}", DateUtils.datePath(),
        FilenameUtils.getBaseName(file.getOriginalFilename()), getWebFileExtName(file));
  }
}
