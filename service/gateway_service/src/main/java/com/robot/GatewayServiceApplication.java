package com.robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
    //令牌桶的开始，定义一个KeyResolver
    @Bean
    public KeyResolver ipKeyResolver(){

        
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return  Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
            }
        };
    }





}
