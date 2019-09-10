package com.web.resolver;

import com.web.annotation.SocialUser;
import com.web.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

/*
    MethodParameter로 해당 파라미터의 정보를 받게 된다.
    파라미터에 @SocialUser 어노테이션이 있고, 타입이 User인 파라미터만 true를 반환할 것이다.
    supportsParameter() 메서드에서 처음 한 번 체크된 부분은 캐시되어 이후의 동일한 호출 시에는
     체크되지 않고 캐시된 결과값을 바로 반환한다.
 */


    /*
    resolveArgument() 메서드는 검증이 완료된 파라미터 정보를 받는다.
    이미 검증이 되어 세션에 해당 User 객체가 있으면,
    User 객체를 구성하는 로직을 수행하지 않도록 세션을 먼저 확인하는 코드를 구현하자.
    세션은 RequestContextHolder를 사용해서 가져올 수 있다.
     */
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(SocialUser.class)
                != null && parameter.getParameterType().equals(User.class);
    }

    public Object resolveArgument (MethodParameter parameter,
                            ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                            WebDataBinderFactory binderFactory) throws Exception {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder
        .currentRequestAttributes()).getRequest().getSession();
        User user = (User) session.getAttribute("user");
        return getUser(user, session);
    }
}
/*
세션에서 인증된 User 객체를 가져온다.
getUser() 메서드를 만들어 세션에서 가져온 User 객체가 없으면 새로 생성하고,
이미 있다면 바로 사용하도록 반환한다.
getUser() 메서드는 인증된 User 객체를 만들어 권한까지 부여하는 코드이기 때문에 현재 코드에서 제외시켰다.
각 소셜 미디어마다 다른 네이밍 방식을 취하고 있기 때문에 코드가 좀 더 길어질 수 있다.
 */