package com.globallogic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = "com.globallogic.*")
public class FavCityApplication {

    public static void main(String[] args) {
        SpringApplication.run(FavCityApplication.class);

    }
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }

}
