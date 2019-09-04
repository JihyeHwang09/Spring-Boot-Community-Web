package com.web.domain;

import com.web.domain.enums.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
public class User implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String principal;

    @Column
    private SocialType socialType;

    @Column
    private LocalDateTime createdDate;
// LocalDateTime: 자바8에 새로 추가된 기능
//    기존에는 Date, Calendar 등을 주로 사용했지만, 날짜 연산 기능이 많이 부족
//  (근거: 날짜에 대한 연산, 비교 등을 API로 제공하는 JodaDateTime을 많이 사용)
//    LocalDateTime이 제공된 이후로는 JodaDateTime 의존성을 따로 포함할 필요 X
//   (이유: LocalDateTime이 대부분의 날짜 기능을 제공하기 때문)

    @Column
    private LocalDateTime updatedDate;

    @Builder
    public User(String name, String password, String email, LocalDateTime createdDate,
                LocalDateTime updatedDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
