package com.web.config;

import com.web.oauth.ClientResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

// 각 소셜 미디어 리소스 정보를 빈으로 등록
public class SecurityConfig {
    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("kakao")
    public  ClientResources kakao() {
        return new ClientResources();
    }
}
/*
    소셜 미디어 리소스 정보는 시큐리티 설정에서 사용하기 때문에 빈으로 등록했고
    3개의 소셜 미디어 프로퍼티를 @ConfigurationProperties 어노테이션에 접두사를 사용하여
    바인딩 했다.
    만약 @ConfigurationProperties 어노테이션이 없었다면, 일일이 프로퍼티값을 불러와야 했을 것이다.
 */