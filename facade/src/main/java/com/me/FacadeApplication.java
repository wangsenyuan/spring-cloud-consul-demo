package com.me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import(FacadeConfiguration.class)
public class FacadeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacadeApplication.class, args);
    }
}
