package com.gobrs.async.core.autoconfig;

import com.gobrs.async.core.*;
import com.gobrs.async.core.cache.GCache;
import com.gobrs.async.core.cache.GCacheManager;
import com.gobrs.async.core.property.GobrsAsyncProperties;
import com.gobrs.async.core.callback.*;
import com.gobrs.async.core.config.*;
import com.gobrs.async.core.engine.RuleEngine;
import com.gobrs.async.core.engine.RuleParseEngine;
import com.gobrs.async.core.engine.RulePostProcessor;
import com.gobrs.async.core.engine.RuleThermalLoad;
import com.gobrs.async.core.holder.BeanHolder;
import com.gobrs.async.core.threadpool.GobrsAsyncThreadPoolFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

import static com.gobrs.async.core.autoconfig.GobrsAutoConfiguration.GOBRS_NAMESPACE;


/**
 * The type Gobrs auto configuration.
 *
 * @program: gobrs
 * @ClassName GobrsAutoConfiguration auto com.gobrs.async.config
 * @description:
 * @author: sizegang
 * @create: 2022 -01-08 18:21
 * @Version 1.0
 */
@Configuration
@AutoConfigureAfter({GobrsPropertyAutoConfiguration.class})
//  GobrsPropertyAutoConfiguration bean 注入之后在注入 GobrsAutoConfiguration
@ConditionalOnProperty(prefix = GobrsAsyncProperties.PREFIX, value = "enable", matchIfMissing = true, havingValue = "true")
// 判断改bean是否应该创建，条件是：在配置文件 {prefix} 中存在 {value}={havingValue} 的配置，matchIfMissing 表示如果不埋满足这个条件
// 那么也符合条件，注入bean
@Import(BeanHolder.class)
// 注入 BeanHolder，其效果和在BeanHolder 上加上 @Configuration 注解
@ComponentScan(value = GOBRS_NAMESPACE)
public class GobrsAutoConfiguration {

    static {
        Environment.env();
    }

    /**
     * The constant GOBRS_NAMESPACE.
     */
    protected static final String GOBRS_NAMESPACE = "com.gobrs.async";

    /**
     * Instantiates a new Gobrs auto configuration.
     */
    public GobrsAutoConfiguration() {
    }

    private GobrsConfig gobrsConfig;

    /**
     * Instantiates a new Gobrs auto configuration.
     *
     * @param gobrsConfig the gobrs config
     */
    public GobrsAutoConfiguration(GobrsConfig gobrsConfig) {
        this.gobrsConfig = gobrsConfig;
    }

    /**
     * Task flow task flow.
     *
     * @return the task flow
     */
    @Bean
    public TaskFlow taskFlow() {
        return new TaskFlow();
    }

    /**
     * Gobrs async thread pool factory gobrs async thread pool factory.
     * 从 yaml 配置文件中获取到和线程池相关的配置，配置共享的线程池对象，以及每个rule 独有的线程池
     * 将这些线程池全部放在 这个工厂中
     * @param gobrsConfig the gobrs config
     * @return the gobrs async thread pool factory
     */
    @Bean
    public GobrsAsyncThreadPoolFactory gobrsAsyncThreadPoolFactory(GobrsConfig gobrsConfig) {
        return new GobrsAsyncThreadPoolFactory(gobrsConfig);
    }


    /**
     * Rule engine rule engine.
     * @ConditionalOnMissingBean 注解解释 RuleEngine 类型的的实现类 的Bean 只能注入一个，
     * 当超过1个的 RuleEngine 的实现类被注入，那么就会抛出异常
     * @param gobrsConfig the gobrs config
     * @param gobrsAsync  the gobrs async
     * @return the rule engine
     */
    @Bean
    @ConditionalOnMissingBean(value = RuleEngine.class)
    public RuleEngine ruleEngine(GobrsConfig gobrsConfig, GobrsAsync gobrsAsync, GCacheManager cacheManager) {
        return new RuleParseEngine(gobrsConfig, gobrsAsync, cacheManager);
    }

    @Bean
    public GCacheManager cacheManager(@Nullable Map<String, GCache<?, ?, ?>> caches) {
        return new GCacheManager(caches);
    }

    /**
     * Config factory config factory.
     *
     * @param gobrsConfig the gobrs config
     * @return the config factory
     */
    @Bean
    public ConfigFactory configFactory(GobrsConfig gobrsConfig) {
        return new ConfigFactory(gobrsConfig);
    }

    /**
     * Config manager config manager.
     *  only matches when beans meeting all the specified requirements are
     *  already contained in the BeanFactory.
     * @return the config manager
     * 对 ConfigFactory 进行的操作，ConfigFactory直接是从spring 的容器中获取的，不是传入的
     */
    @ConditionalOnBean(ConfigFactory.class)
    @Bean
    public ConfigManager configManager() {
        return new ConfigManager();
    }

    /**
     * Rule engine post processor rule post processor.
     * 解析 rule
     * @param configManager the config manager
     * @return the rule post processor
     */
    @Bean
    public RulePostProcessor ruleEnginePostProcessor(ConfigManager configManager) {
        return new RulePostProcessor(configManager);
    }

    /**
     * Gobrs async gobrs async.
     * 任务触发器，执行任务的
     * @return the gobrs async
     */
    @Bean
    public GobrsAsync gobrsAsync() {
        return new GobrsAsync();
    }

    /**
     * Gobrs spring bean holder.
     *
     * @return the bean holder
     */
    @Bean
    public BeanHolder gobrsSpring() {
        return new BeanHolder();
    }

    /**
     * Async exception interceptor async task exception interceptor.
     *
     * @return the async task exception interceptor
     */
    @Bean
    @ConditionalOnMissingBean(value = AsyncTaskExceptionInterceptor.class)
    public AsyncTaskExceptionInterceptor asyncExceptionInterceptor() {
        return new DefaultAsyncExceptionInterceptor();
    }

    /**
     * Async task pre interceptor async task pre interceptor.
     *
     * @return the async task pre interceptor
     */
    @Bean
    @ConditionalOnMissingBean(value = AsyncTaskPreInterceptor.class)
    public AsyncTaskPreInterceptor asyncTaskPreInterceptor() {
        return new DefaultAsyncTaskPreInterceptor();
    }

    /**
     * Async task post interceptor async task post interceptor.
     *
     * @return the async task post interceptor
     */
    @Bean
    @ConditionalOnMissingBean(value = AsyncTaskPostInterceptor.class)
    public AsyncTaskPostInterceptor asyncTaskPostInterceptor() {
        return new DefaultAsyncTaskPostInterceptor();
    }

    /**
     * Rule thermal loading rule thermal load.
     *
     * @return the rule thermal load
     */
    @Bean
    public RuleThermalLoad ruleThermalLoading() {
        return new RuleThermalLoad();
    }
}
