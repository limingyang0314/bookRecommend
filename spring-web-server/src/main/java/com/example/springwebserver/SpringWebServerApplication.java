package com.example.springwebserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;


@SpringBootApplication(scanBasePackages = {"com.example.springwebserver"},exclude={SecurityAutoConfiguration.class,SecurityFilterAutoConfiguration.class})
@MapperScan("com.example.springwebserver.dao")
public class SpringWebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebServerApplication.class, args);
    }

}
