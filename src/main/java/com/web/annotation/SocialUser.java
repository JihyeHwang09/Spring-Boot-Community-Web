package com.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SocialUser {
}
// SocialUser 어노테이션이 UserArgumentResolver의 supportsParameter() 메서드에서
// @SocialUser를 명시했는지 체크하도록 수정한다.
