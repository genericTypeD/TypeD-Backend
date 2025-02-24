package com.generic.typed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 모든 클라이언트 허용 접근
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*") // 모든 header 허용
                .exposedHeaders("Authorization", "RefreshToken")
                .allowCredentials(true); // 모바일 앱 지원은 credentials 불필요
    }
}