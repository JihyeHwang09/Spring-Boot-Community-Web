package com.web.config;

import com.web.domain.enums.SocialType;

import com.web.oauth.ClientResources;

import com.web.oauth.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import java.util.ArrayList;
import java.util.List;

import static com.web.domain.enums.SocialType.FACEBOOK;
import static com.web.domain.enums.SocialType.GOOGLE;
import static com.web.domain.enums.SocialType.KAKAO;


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
/*
EnableOAuth2Client
: EnableOAuth2Client 어노테이션을 붙여서 적용한다.
참고로 EnableOAuth2Client 이외에도 OAuth2의 권한 부여 서비스와 리소스 서버를 만드는 설정 어노테이션인
@EnableAuthorizationServer, @EnableResourceServer도 있다.
(이 책에서는 권한 및 User 정보를 가져오는 서버를 직접 구성하지는 않고,
모두 각 소셜 미디어의 서버를 사용하기 때문에 두 어노테이션을 사용할 필요는 X)
 */
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("oauth2ClientContext")
    private OAuth2ClientContext oAuth2ClientContext;

    @Override
//        오버라이드한 configure() 메서드의 설정 프로퍼티
    protected void configure(HttpSecurity http) throws Exception {
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
                    .addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class)
                    .csrf().disable();
    }

    /*
    oauth2ClientFilterRegistration( OAuth2ClientContextFilter filter) 메서드
    : OAuth2 클라이언트용 시큐리티 필터인 OAuth2ClientContextFilter를 불러와서
    올바른 순서로 필터가 동작하도록 설정한다.
    스프링 시큐리티 필터가 실행되기 전에 충분히 낮은 순서로 필터를 등록한다.
     */
    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(
            OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    /*
    oauth2Filter()메서드
    : 오버로드하여 2개가 정의되어 있다.
    1) oauth2Filter()와 2) oauth2Filter(ClientResources client, String path, SocialType socialType)
     */
    /*
    1) oauth2Filter()메서드
    : 각 소셜 미디어 타입을 받아 필터 설정을 할 수 있다.
     */
    private Filter oauth2Filter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(oauth2Filter(facebook(), "/login/facebook", FACEBOOK));
        filters.add(oauth2Filter(google(), "/login/google", GOOGLE));
        filters.add(oauth2Filter(kakao(), "/login/kakao", KAKAO));
        filter.setFilters(filters);
        return filter;
    }

    /*
    2)  oauth2Filter(ClientResources client, String path, SocialType socialType) 메서드
    : 각 소셜 미디어 필터를 리스트 형식으로 반환한다.
     */
    private Filter oauth2Filter(ClientResources client, String path,
                                SocialType socialType) {
//      1. 인증이 수행될 경로를 넣어 OAuth2 클라이언트용 인증 처리 필터를 생성한다.
        OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter(path);
//      2. 권한 서버와의 통신을 위해 OAuth2RestTemplate을 생성한다.
//        이를 생성하기 위해선 client 프로퍼티 정보와 OAuth2ClientContext가 필요하다.
        OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oAuth2ClientContext);
        filter.setRestTemplate(template);
//      3. User 권한을 최적화해서 생성하고자 UserInfoTokenServices를 상속받은 UserTokenService를 생성했다.
//        OAuth2 AccessToken 검증을 위해 생성한 UserTokenService를 필터의 토큰 서비스로 등록한다.

/*       User 정보를 비동기 통신으로 가져오는 REST Service인 UserInfoTokenServices를 커스터마이징할
        UserTokenService를 생성해보자.
        소셜 미디어 원격 서버와 통신하여 User 정보를 가져오는 로직은 이미 UserInfoTokenServices에 구현되어 있어
        UserTokenService에서는 이를 상속받아 통신에 필요한 값을 넣어주어 설정하면 된다.
*/

        filter.setTokenServices(new UserTokenService(client, socialType));
//      4. 인증이 성공적으로 이루어지면, 필터에 리다이렉트될 URL을 설정한다.
        filter.setAuthenticationSuccessHandler((request, response, authentication)
                -> response.sendRedirect("/" + socialType.getValue() +
                "/complete"));
//      5. 인증이 실패하면, 필터에 리다이렉트될 URL을 설정한다.
        filter.setAuthenticationFailureHandler((request, response, exception) ->
                response.sendRedirect("/error"));
                return filter;
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