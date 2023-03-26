package com.microservice.erp.webapp.config;

import com.infoworks.lab.cache.MemCache;
import com.infoworks.lab.datasources.RedisDataSource;
import com.infoworks.lab.datasources.RedissonDataSource;
import com.microservice.erp.domain.beans.models.Otp;
import com.microservice.erp.domain.models.LoginRetryCount;
import com.it.soul.lab.data.base.DataSource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class BeanConfig {

    private Environment env;

    public BeanConfig(Environment env) {
        this.env = env;
    }

    @Bean
    RedissonClient getRedisClient(){
        String redisHost = env.getProperty("app.redis.host") != null
                ? env.getProperty("app.redis.host") : "localhost";
        String redisPort = env.getProperty("app.redis.port") != null
                ? env.getProperty("app.redis.port") : "6379";
        String redisPassword = env.getProperty("app.redis.password") != null
                ? env.getProperty("app.redis.password") : "gyalsung@2023";
        Config conf = new Config();
        conf.useSingleServer()
                .setAddress(String.format("redis://%s:%s",redisHost, redisPort))
                .setRetryAttempts(5)
                .setPassword(redisPassword)
                .setRetryInterval(1500);
        //Redisson-Client instance are fully-thread safe.
        return Redisson.create(conf);
    }

    @Bean("loginRetryCount")
    DataSource<String, LoginRetryCount> getLoginRetryCache(RedissonClient client) {
        long ttl = Long.valueOf(env.getProperty("app.login.retry.block.duration.millis")) * 2;
        RedisDataSource dataSource = new RedissonDataSource(client, ttl);
        return new MemCache<>(dataSource, LoginRetryCount.class);
    }

    @Bean("otpCache")
    DataSource<String, Otp> getOtpCache(RedissonClient client) {
        long ttl = Long.valueOf(env.getProperty("app.otp.ttl.minute"));
        RedisDataSource dataSource = new RedissonDataSource(client, Duration.ofMinutes(ttl).toMillis());
        return new MemCache<>(dataSource, Otp.class);
    }

    @Bean("notifyTemplate")
    public RestTemplate getNotifyTemplate(@Value("${app.notify.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

    @Bean("mailTemplate")
    public RestTemplate getMailTemplate(@Value("${app.notify.mail.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

    @Bean("mailOtpTemplate")
    public RestTemplate getMailOtpTemplate(@Value("${app.notify.mail.otp.url}") String url) {
        return new RestTemplateBuilder()
                .rootUri(url)
                .build();
    }

}
