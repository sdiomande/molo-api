package com.map.moloapi.configs;

import com.map.moloapi.securities.AuthEntryPointJwt;
import com.map.moloapi.securities.AuthTokenFilter;
import com.map.moloapi.securities.UserDetailsServiceImpl;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 03/11/2023 10:28
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    @Autowired
    UserDetailsServiceImpl userService;
    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Autowired
    AuthEntryPointJwt authEntryPointJwt;

    @Autowired
    Environment env;

    private String URI_TO_IGNORE[] = {
            "/auth/**",
            "/reports/**",
//            "/auth/verify-reset-password-token/**",
//            "/partners/**",
            "/tests/**"
    };

    public static String SWAGGER_URI_TO_IGNORE[] = {
            "/swagger/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/v2/**",
            "/api-docs/**"
    };

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(b -> b.authenticationEntryPoint(authEntryPointJwt))
                .authorizeHttpRequests(request -> {
                    if (Arrays.stream(env.getActiveProfiles()).anyMatch(p -> p.equalsIgnoreCase("local") || p.equalsIgnoreCase("stagging"))) {
                        log.info("-- Swagger disponible --");
                        request.requestMatchers(SWAGGER_URI_TO_IGNORE).permitAll();
                    } else {
                        log.info("-- Swagger non disponible --");
                    }

                    request.requestMatchers(URI_TO_IGNORE)
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info()
                        .title("SOCOPRIM INTERNAL API")
                        .description("SOCOPRIM INTERNAL API - RECHARGEMENT DES BADGES PONT HKB VIA PORTEFEUILLE ELECTRONIQUE")
                        .version("1.0")
                        .contact(new Contact()
                                .name("SOCITECH BUSINESS SOLUTIONS")
                                .email("support.sbs@socitech.com")
                                .url("https://www.socitechbusiness.com"))
                        .license(new License()
                                .name("License API")
                                .url("https://www.socitechbusiness.com")));
    }
}