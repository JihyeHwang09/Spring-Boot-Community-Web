package com.web.domain.enums;
/*
각 소셜 미디어의 정보를 나타내는 SocailType enum을 생성
 -> enum을 사용해 권한 생성 로직을 공통 코드로 처리하여 중복 코드를 줄일 수 있다.
 */
public enum SocialType {
    FACEBOOK("facebook");
    GOOGLE("google");
    KAKAO("kkakao");

    private final String ROLE_PREFIX = "ROLE_";
    private String name;

    SocialType(String name) {
        this.name = name;
    }

    public String getRoleType() {return ROLE_PREFIX + name.toUpperCase();}

    public String getValue() {
        return name;
    }
    public boolean isEquals(String authority) {
        return this.getRoleType().equals(authority);
    }
}
