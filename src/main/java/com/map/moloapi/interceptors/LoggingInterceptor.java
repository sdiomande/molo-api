package com.map.moloapi.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;


/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-partner-api
 * @Date 30/10/2023 15:19
 */
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    public static List<String> SWAGGER_URI_TO_IGNORE = Arrays.asList(
            "/swagger-ui/",
            "/v3/api-docs",
            "/swagger-resources/",
            "/v2/",
            "/api-docs",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        SWAGGER_URI_TO_IGNORE.forEach(e -> {
//            if (!request.getRequestURI().contains(e)) {
//                log.info("HTTP [{}] : {}", request.getMethod(), request.getRequestURI());
//            }
//        });
        if (/*!request.getRequestURI().contains("swagger") &&*/ !SWAGGER_URI_TO_IGNORE.contains(request.getRequestURI())) {
            log.info("HTTP [{}] : {}", request.getMethod(), request.getRequestURI());
        }
        return true;
    }
}
