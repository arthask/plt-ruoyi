package com.example.filespringbootstarter.config.local;

import com.example.filespringbootstarter.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@ConditionalOnBean(name = {"localFileClientConfig"})
@Configuration
public class LocalResourcesConfig implements WebMvcConfigurer {

    @Autowired
    private LocalFileClientConfig localFileClientConfig;

    /**
     * 本地文件资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(Constants.RESOURCE_PREFIX + "/**")
                .addResourceLocations("file:" + localFileClientConfig.getBasePath() + "/");
    }
}
