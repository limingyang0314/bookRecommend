package com.example.springwebserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.example.springwebserver"})
@MapperScan("com.example.springwebserver.dao")
public class SpringWebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebServerApplication.class, args);
    }

}
