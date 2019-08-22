package com.web.domain.enums;
// 스프링이 관리하는 컴포넌트에서 퍼시스턴스 계층에 대해 명확하게 명시하는 특수한 제네릭 스트레오 타입을 말한다.
// * 퍼시스턴스 계층: 물리적인 저장공간. 영속성을 가진 파일이나 DB에 로직을 구현하는 것을 의미

public enum BoardType {
    notice("공지사항"),
    free("자유게시판");

    private String value;

    BoardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
