package com.autotrade.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.autotrade")
@ComponentScan(basePackages = "com.autotrade")
public class AutotradeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutotradeServiceApplication.class, args);
    }
}
