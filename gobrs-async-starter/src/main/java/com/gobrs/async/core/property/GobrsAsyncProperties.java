package com.gobrs.async.core.property;


import com.gobrs.async.core.common.constant.ConfigPropertiesConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.gobrs.async.core.common.def.DefaultConfig.*;

/**
 * The type Gobrs async properties.
 *
 * @author sizegang1
 * @program: gobrs
 * @ClassName BootstrapProperties
 * @description:
 * @author: sizegang
 * @create: 2022 -01-08 17:30
 * @Version 1.0
 * @date 2022 -01-27 22:04   gobrs.yaml 中 gobrs.async.config 后面的内容和这类中的属性进行序列化
 * adding a PropertySource to Spring's Environment.
 * PropertySource 指定读取那些文件，默认值读取 properties 文件
 * factory 可以自定义读取任意格式的文件
 * ConfigurationProperties：指定读取文件中配置的前缀，非配置前缀的内容会被过滤，不读取
 * 默认可以不用这个注解 PropertySource，读取 application.yaml
 * 有了这个注解，就可以读取任何路径下的配置文件
 */
@ConfigurationProperties(prefix = GobrsAsyncProperties.PREFIX, ignoreInvalidFields = false)
@PropertySource(value = {"classpath:config/gobrs.yaml", "classpath:config/gobrs.yml", "classpath:config/gobrs.properties"}, ignoreResourceNotFound = false, factory = GobbrsPropertySourceFactory.class)
@Component
@Data
public class GobrsAsyncProperties {

    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = ConfigPropertiesConstant.PREFIX;


    private boolean enable;
    /**
     * Task rules
     */
    private List<RuleConfig> rules;


    private PlatformConfig platform;

    /**
     * Task separator
     */
    private String split = ";";

    /**
     * Next com.gobrs.async.com.gobrs.async.test.task
     */
    private String point = "->";


    private ThreadPool threadPool;
    /**
     * 超时时间监听时间
     */
    private Integer timeoutCoreSize;


    /**
     * Whether global parameter dataContext mode  Parameter context
     */
    private boolean paramContext = true;

    private boolean catchable;
    /**
     * Default timeout
     *
     * @return
     */
    private long timeout = 3000;


    private boolean relyDepend = false;


    /**
     * The type Thread pool. 线程池的配置
     */
    @Data
    public static class ThreadPool {
        /**
         * number of core threads
         */
        private Integer corePoolSize = calculateCoreNum();

        /**
         * maximum number of threads
         */
        private Integer maxPoolSize = corePoolSize + (corePoolSize >> 1);

        /**
         * thread survival time
         */
        private Long keepAliveTime = KEEPALIVETIME;

        /**
         * thread survival time unit
         */
        private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

        /**
         * Thread execution timeout
         */
        private Long executeTimeOut = EXECUTETIMEOUT;

        /**
         * queue maximum capacity
         */
        private Integer capacity = THREADPOOLQUEUESIZE;


        /**
         * blocking queue
         */
        private BlockingQueue workQueue = new LinkedBlockingQueue(capacity);

        /**
         * Reject com.gobrs.async.com.gobrs.async.test.task policy when thread pool com.gobrs.async.com.gobrs.async.test.task is full
         */
        private String rejectedExecutionHandler = "AbortPolicy";

        /**
         * thread name prefix
         */
        private String threadNamePrefix;


        /**
         * Allow core threads to time out
         */
        private Boolean allowCoreThreadTimeOut = false;

        private Integer calculateCoreNum() {
            int cpuCoreNum = Runtime.getRuntime().availableProcessors();
            return new BigDecimal(cpuCoreNum).divide(new BigDecimal("0.2")).intValue();
        }



    }
}