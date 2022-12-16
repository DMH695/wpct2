package com.tbxx.wpct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author ZXX
 * @ClassName WpctApplication
 * @Description
 * @DATE 2022/9/29 18:18
 */

@MapperScan("com.tbxx.wpct")
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
public class WpctApplication {
    public static void main(String[] args) {
        SpringApplication.run(WpctApplication.class, args);
    }
}
