package com.web.resolver;

import org.springframework.core.MethodParameter;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    public Object resolveArgument (MethodParameter parameter,
                                   ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                   WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }
}
