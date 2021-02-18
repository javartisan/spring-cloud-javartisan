package com.javartisan.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class JavartisanRegisterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavartisanRegisterApplication.class, args);
    }

}
