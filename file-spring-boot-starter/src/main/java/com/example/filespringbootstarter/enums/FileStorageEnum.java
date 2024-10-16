package com.example.filespringbootstarter.enums;

import com.example.filespringbootstarter.config.FileClientConfig;
import com.example.filespringbootstarter.config.local.LocalFileClientConfig;
import com.example.filespringbootstarter.config.s3.S3FileClientConfig;
import com.example.filespringbootstarter.core.client.FileClient;
import com.example.filespringbootstarter.core.client.local.LocalFileClient;
import com.example.filespringbootstarter.core.client.s3.S3FileClient;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileStorageEnum {

  LOCAL(10, LocalFileClientConfig.class, LocalFileClient.class),

  S3(20, S3FileClientConfig.class, S3FileClient.class),;

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
