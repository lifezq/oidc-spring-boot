package com.yql.oidc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @Package com.yql.oidc.config
 * @ClassName WebConfig
 * @Description TODO
 * @Author Ryan
 * @Date 3/20/2023
 */
@Configuration
public class WebConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
