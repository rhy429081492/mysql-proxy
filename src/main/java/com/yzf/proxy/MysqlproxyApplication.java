package com.yzf.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.yzf.proxy.service")
public class MysqlproxyApplication {
    public static void main(String[] args){
        SpringApplication.run(MysqlproxyApplication.class, args);
    }
}
