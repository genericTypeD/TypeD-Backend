package com.generic.typed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.generic.typed", "com.generic.kakaoApi"})
public class TypedApplication {

    public static void main(String[] args) {
        SpringApplication.run(TypedApplication.class, args);
    }

}
