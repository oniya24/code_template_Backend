package com.code_template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 请求pattern
                .allowedOrigins("*") // 请求源
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS") // 请求方法
                .allowCredentials(true) // 允许cookies？
                .maxAge(3600)
                .allowedHeaders("*"); // 请求头
    }
}
