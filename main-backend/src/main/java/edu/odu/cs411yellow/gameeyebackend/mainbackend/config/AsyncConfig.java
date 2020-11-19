package edu.odu.cs411yellow.gameeyebackend.mainbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class for thread pool.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    /**
     * Creates async thread pool bean. Allows executing methods asynchronously.
     *
     * @return Async executor.
     */
    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setThreadNamePrefix("AsyncWorker-");
        executor.initialize();

        return executor;
    }
}
