package com.globallogic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.globallogic.*")
public class UserModuleApplication {
    public static void main(String[] args) {

        SpringApplication.run(UserModuleApplication.class);
    }
}
