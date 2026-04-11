package com.otaku.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ECommerceOtakuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceOtakuApplication.class, args);
    }

}
