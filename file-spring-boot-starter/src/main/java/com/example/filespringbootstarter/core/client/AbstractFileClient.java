package com.example.filespringbootstarter.core.client;

import com.example.filespringbootstarter.config.FileClientConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件客户端的抽象类，提供模板方法，减少子类的冗余代码
 *
 * @author author
 */
@Slf4j
public abstract class AbstractFileClient<Config extends FileClientConfig> implements FileClient {

    /**
     * 文件配置
     */
    protected Config config;

    public AbstractFileClient(Config config) {
        this.config = config;
        this.init();
    }

    /**
     * 初始化
     */
    public final void init() {
        doInit();
        log.debug("[init][配置({}) 初始化完成]", config);
    }

    /**
     * 自定义初始化
     */
    protected abstract void doInit();
}
