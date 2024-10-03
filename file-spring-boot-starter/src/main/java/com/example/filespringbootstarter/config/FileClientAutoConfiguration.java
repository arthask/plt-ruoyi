package com.example.filespringbootstarter.config;

import com.example.filespringbootstarter.core.service.FileService;
import com.example.filespringbootstarter.core.service.FileServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(value = "file.storage.use", matchIfMissing = false)
@Configuration
@ConfigurationPropertiesScan(basePackages = "com.example.filespringbootstarter.config")
public class FileClientAutoConfiguration {
    @Bean
    public FileService fileService() {
        return new FileServiceImpl();
    }
}
