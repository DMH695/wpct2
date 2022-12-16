package com.tbxx.wpct.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ZXX
 * @ClassName RedissonConfig
 * @Description  Redisson分布式锁
 * @DATE 2022/9/30 12:07
 */
//
//@Configuration
//public class RedissonConfig {
//    @Bean
//    public RedissonClient redissonClient(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://192.168.195.129:6379")
//                .setPassword("wcw191002")
//                .setDatabase(1);
//        // 创建RedissonClient对象
//        return Redisson.create(config);
//    }
//}
