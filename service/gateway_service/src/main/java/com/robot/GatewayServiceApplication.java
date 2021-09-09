package com.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
@EnableEurekaClient
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
        String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        String hashpw = BCrypt.hashpw("123456", gensalt);
        System.out.println("用盐加密后的密码"+hashpw);

    }

}
