package com.web.config;

import com.web.oauth.ClientResources;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

// 각 소셜 미디어 리소스 정보를 빈으로 등록
@Configuration
/*
@EnableWebSecurity
: 웹에서 시큐리티 기능을 사용하겠다는 어노테이션
스프링 부트에서는 @EnableWebSecurity를 사용하면 자동 설정이 적용된다.
 */
@EnableWebSecurity
/*
WebSecurityConfigurerAdapter
: 자동 설정 그대로 사용할 수도 있지만 요청, 권한, 기타 설정에 대해서는 필수적으로 최적화한 설정이 들어가야 한다.
최적화 설정을 위해 WebSecurityConfigurerAdapter를 상속 받고 configure(HttpSecurity http) 메서드를 오버라이드하여
원하는 형식의 시큐리티 설정을 한다.
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        /*
        오버라이드한 configure() 메서드의 설정 프로퍼티에 대한 설명이다.
        * authorizeRequests(): 인증 메커니즘을 요청한 HttpServletRequest 기반으로 설정한다.
        - antMatchers(): 요청 패턴을 리스트 형식으로 설정한다.
        - permitAll(): 설정한 리퀘스트 패턴을 누구나 접근할 수 있도록 허용
        - anyRequest(): 설정한 요청 이외의 리퀘스트 요청을 표현한다.
        - authenticated(): 해당 요청은 인증된 사용자만 사용할 수 있다.
         */
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http
//               authorizeRequests(): 인증 메커니즘을 요청한 HttpServletRequest 기반으로 설정한다.
                .authorizeRequests()
//                  antMatchers(): 요청 패턴을 리스트 형식으로 설정한다.
                    .antMatchers("/", "/login/**", "/css/**", "/images/**", "/js/**",
//                                           permitAll(): 설정한 리퀘스트 패턴을 누구나 접근할 수 있도록 허용
                            "/console/**").permitAll()
        //        anyRequest(): 설정한 요청 이외의 리퀘스트 요청을 표현한다.
        //        authenticated(): 해당 요청은 인증된 사용자만 사용할 수 있다.
                    .anyRequest().authenticated()
                .and()
//                   headers(): 응답에 해당하는 header를 설정한다. 설정하지 않으면, 디폴트값으로 설정된다.
//                   frameOptions().disable(): XFrameOptionsHeaderWriter 최적화 설정을 허용하지 않는다.
                    .headers().frameOptions().disable()
                .and()
                    .exceptionHandling()
/*
                    authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                   : 인증의 진입 지점
                   인증되지 않은 사용자가 허용되지 않은 경로로 리퀘스틀 요청할 경우 '/login'으로 이동된다.
 */
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(
                        "/login"
                                    ))
                .and()
//                  formLogin().successForwardUrl("/board/list")
//                  : 로그인에 성공하면 설정된 경로로 포워딩 된다.
                    .formLogin()
                    .successForwardUrl("/board/list")
                .and()
//                   logout(): 로그아웃에 대한 설정을 할 수 있다.
                    .logout()
//                   logoutUrl: 코드에서는 로그아웃이 수행될 URL
                    .logoutUrl("/logout")
//                  .logoutSuccessUrl("/"): 로그아웃이 성공했을 때 포워딩될 URL
                    .logoutSuccessUrl("/")
//                   deleteCookies: 로그아웃을 성공했을 때 삭제될 쿠키값
                    .deleteCookies("JSESSIONID")
//                   invalidateHttpSession: 설정된 세션의 무효화를 수행하게끔 설정되어 있다.
                    .invalidateHttpSession(true)
                .and()
//                  .addFilterBefore(filter, CsrfFilter.class)
//                  : 문자 인코딩 필터(filter)보다 CsrfFilter를 먼저 실행하도록 설정한다.
                    .addFilterBefore(filter, CsrfFilter.class)
                    .csrf().disable();
    }

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

    만약 @ConfigurationProperties 어노테이션이 없었다면,
    일일이 프로퍼티값을 불러와야 했을 것이다.
 */