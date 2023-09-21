package com.test;

import io.seata.spring.annotation.GlobalTransactional;
import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.test.mapper")
@EnableDiscoveryClient
@EnableAutoDataSourceProxy
public class BookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class,args);
    }
}
