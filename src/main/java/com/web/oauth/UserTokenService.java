package com.web.oauth;

import com.web.domain.enums.SocialType;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;
import java.util.Map;

/*
UserInfoTokenServices를 상속받은 UserTokenService 클래스를 생성했다.
* UserInfoTokenServices
: 스프링 시큐리티 OAuth2에서 제공하는 클래스
    User 정보를 얻어오기 위해 소셜 서버와 통신하는 역할을 수행한다.
    이때 URI와 clientId 정보가 필요하다.
    이 책에서는 3개의 소셜 미디어 정보를 SocialType을 기준으로 관리할 것이기 때문에
    약간의 커스터마이징이 필요하다.
 */
public class UserTokenService extends UserInfoTokenServices {

    public UserTokenService(ClientResources resources, SocialType socialType) {
//        UserInfoTokenServices 생성자에서 super()를 사용하여
//        각각의 소셜 미디어 정보를 주입할 수 있도록 한다.
        super(resources.getResource().getUserInfoUri(), resources.getClient().getClientId());
        setAuthoritiesExtractor(new OAuth2AuthoritiesExtractor(socialType));
    }
//    OAuth2AuthoritiesExtractor 클래스
//      : AuthoritiesExtractor 인터페이스를 구현한 내부 클래스인 OAuth2AuthoritiesExtractor를 생성함
    public static class OAuth2AuthoritiesExtractor implements AuthoritiesExtractor {

        private String socialType;
//      권한 생성 방식을 'ROLE_FACEBOOK'으로 하기 위해 SocialType의 getRoleType() 메소드를 사용
        /*
        UserTokenService의 부모 클래스인 UserInfoTokenServices(마지막 스페링 's' 하나 차이)
        의 setAuthoritiesExtractor() 메서드를 이용하여 등록한다.
        */
        public OAuth2AuthoritiesExtractor(SocialType socialType) {
            this.socialType = socialType.getRoleType();
        }

//        extractAuthorities() 메서드를 오버라이드하여 권한을 리스트로 생성하여 반환하도록 함
        @Override
        public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
            return AuthorityUtils.createAuthorityList(this.socialType);
        }
    }
}
//
