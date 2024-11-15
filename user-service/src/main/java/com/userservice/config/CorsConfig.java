package com.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8111"); // Swagger UI를 제공하는 Gateway Origin
        config.addAllowedOrigin("http://gachon-adore.duckdns.org:8111");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*"); // GET, POST 등 모든 HTTP 메소드 허용
        config.setAllowCredentials(true); // 인증 정보를 포함한 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 CORS 적용
        return new CorsFilter(source);
    }
}