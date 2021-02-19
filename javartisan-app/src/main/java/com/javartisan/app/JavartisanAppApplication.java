package com.javartisan.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.javartisan"})
/**
 * ComponentScan 该注解是不会处理@FeignClient注解的扫描。需要使用EnableFeignClients指定@FeignClient扫描包路径
 *
 * @ComponentScan(basePackages = {"com.javartisan"})
 */
public class JavartisanAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavartisanAppApplication.class, args);
    }

}
