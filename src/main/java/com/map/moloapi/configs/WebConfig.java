package com.map.moloapi.configs;

import com.map.moloapi.interceptors.AuditLogInterceptor;
import com.map.moloapi.interceptors.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 03/11/2023 10:28
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
        registry.addInterceptor(new AuditLogInterceptor());
    }
}
