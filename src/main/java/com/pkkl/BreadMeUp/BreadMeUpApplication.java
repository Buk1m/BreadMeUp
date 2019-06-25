package com.pkkl.BreadMeUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BreadMeUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreadMeUpApplication.class, args);
    }

}
