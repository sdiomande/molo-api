package com.map.moloapi.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.client.RestClientBuilderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 06/03/2024 13:16
 */
@Slf4j
@Configuration
public class RestClientConfig {

    @Bean("base")
    public RestClient pplapi(RestClient.Builder builder) {
        return builder.build();
    }
}
