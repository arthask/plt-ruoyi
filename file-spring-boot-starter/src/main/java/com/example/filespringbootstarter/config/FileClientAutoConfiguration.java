package com.example.filespringbootstarter.config;

import com.example.filespringbootstarter.config.local.LocalFileClientConfig;
import com.example.filespringbootstarter.config.s3.S3FileClientConfig;
import com.example.filespringbootstarter.core.factory.FileClientFactoryImpl;
import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.core.service.FileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "file.storage.enable", matchIfMissing = false)
@Configuration
@ConfigurationPropertiesScan(basePackages = "com.example.filespringbootstarter.config")
public class FileClientAutoConfiguration {

    @ConditionalOnProperty(value = "file.storage.local.use")
    @Bean
    public FileClientConfig localFileClientConfig() {
        return new LocalFileClientConfig();
    }

    @ConditionalOnProperty(value = "file.storage.s3.use")
    @Bean
    public FileClientConfig s3FileClientConfig() {
        return new S3FileClientConfig();
    }

    @Bean
    @ConditionalOnBean(name = {"localFileClientConfig", "s3FileClientConfig"})
    public FileService complexFileService() {
        return new FileServiceImpl(new FileClientFactoryImpl(localFileClientConfig(), s3FileClientConfig()));
    }

    @Bean
    @ConditionalOnBean(name = {"localFileClientConfig"})
    @ConditionalOnMissingBean(name = {"s3FileClientConfig"})
    public FileService localFileService() {
        return new FileServiceImpl(new FileClientFactoryImpl(localFileClientConfig(), null));
    }

    @Bean
    @ConditionalOnBean(name = {"s3FileClientConfig"})
    @ConditionalOnMissingBean(name = {"localFileClientConfig"})
    public FileService s3FileService() {
        return new FileServiceImpl(new FileClientFactoryImpl(null, s3FileClientConfig()));
    }
}
