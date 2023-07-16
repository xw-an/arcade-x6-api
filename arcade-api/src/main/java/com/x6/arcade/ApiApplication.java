package com.x6.arcade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.x6.arcade.*")
//@EnableDiscoveryClient
@SpringBootApplication
public class ApiApplication {

        public static void main(String[] args) {
            SpringApplication.run(ApiApplication.class, args);
        }
}
