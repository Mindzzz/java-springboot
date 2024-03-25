package com.mindzzz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.mindzzz.mapper")
@SpringBootApplication
public class HappyLifeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyLifeApplication.class, args);
    }

}
