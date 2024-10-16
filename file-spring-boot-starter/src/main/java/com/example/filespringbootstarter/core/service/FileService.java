package com.example.filespringbootstarter.core.service;


import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.filespringbootstarter.enums.FileStorageEnum;

/**
 * 文件 Service 接口
 *
 * @author 芋道源码
 */
public interface FileService {

  /**
   * 保存文件，并返回文件的访问路径
   *
   * @param name 文件名称
   * @param path 文件路径
   * @param content 文件内容
   * @return 文件路径
   */
  String createFile(FileStorageEnum fileStorageEnum, String name, String path, byte[] content);

  String createWebFile(FileStorageEnum fileStorageEnum, MultipartFile file) throws IOException;


  /**
   * 删除文件
   * 
   * @param fileStorageEnum 配置编号
   * @param path 文件路径
   */
  void deleteFile(FileStorageEnum fileStorageEnum, String path) throws Exception;

  /**
   * 获得文件内容
   *
   * @param fileStorageEnum 配置编号
   * @param path 文件路径
   * @return 文件内容
   */
  byte[] getFileContent(FileStorageEnum fileStorageEnum, String path) throws Exception;


}
