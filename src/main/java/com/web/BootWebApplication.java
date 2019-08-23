package com.web;

import com.web.domain.Board;
import com.web.domain.User;
import com.web.domain.enums.BoardType;
import com.web.repository.BoardRepository;
import com.web.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
public class BootWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootWebApplication.class, args);
    }
/*
    애플리케이션 구동 후 CommandLineRunner로 테스트용 데이터를 DB에 넣어보자.
    CommandLineRunner는 애플리케이션 구동 후 특정 코드를 실행시키고 싶을 때 직접 구현하는 인터페이스이다.
    애플리케이션 구동 시 테스트 데이터를 함께 생성하여 데모 프로젝트를 실행/테스트하고 싶을 때 편리하다.
    또한, 여러 CommandLineRunner를 구현하여 같은 애플리케이션 컨텍스트의 빈에 등록할 수 있다.
*/
    @Bean
//    스프링은 빈으로 생성된 메서드에 파라미터로 DI(Dependency Injection) 시키는 메커니즘이 존재한다.
//    생성자를 통해 의존성을 주입시키는 방법과 유사하다.
//    이를 이용하여 CommandLineRunner를 빈으로 등록한 후 UserRepository와 BoardRepository를 주입받는다.
    /* DI(Dependency Injection): 스프링의 주요 특성 중 하나.
    * 주로 의존 관계 주입이라고 한다.
    * 또는 의존 관계를 주입하는 게 아니라 단지 객체의 레퍼런스를 전달하여 참조시킨다는 의미로
    * 의존 관계 설정이라고도 한다.
    */
    public CommandLineRunner runner(UserRepository userRepository,
                                    BoardRepository boardRepository) throws Exception {
        return (args) -> {
/*
    메서드 내부에 실행이 필요한 코드를 작성한다.
    User 객체를 빌더 패턴(Builder Pattern)을 사용하여 생성한 후
     주입받은 UserRepository를 사용하여 User 객체를 저장한다.
*/
/*
    빌더 패턴(Builder Pattern): 객체의 생성 과정과 표현 방법을 분리하여
    객체를 단계별 동일한 생성 절차로 복잡한 객체를 만드는 패턴
 */
            User user = userRepository.save(User.builder()
            .name("havi")
            .password("test")
            .email("havi@gmail.com")
            .createdDate(LocalDateTime.now())
            .build());
/*
* 페이징 처리 테스트를 위해 위와 동일하게 빌더 패턴을 사용한다.
* IntStream의 rangeClosed를 사용하여 index 순서대로 Board 객체 200개를 생성하여 저장한다.
*/
            IntStream.rangeClosed(1, 200).forEach(index ->
                    boardRepository.save(Board.builder()
                        .title("게시글" + index)
                        .boardType(BoardType.free)
                        .subTitle("순서" + index)
                        .content("콘텐츠")
                        .createdDate(LocalDateTime.now())
                        .updatedDate(LocalDateTime.now())
                            .user(user).build())
            );
        };
    }
}
/*
* CommandLineRunner와 자바8 표현식을 깔끔하게 원하는 구현하였다.
* CommandLineRunner는 위의 방법 외의 방법으로도 구현할 수 있지만,
* 어떤 방법을 사용하든 빈으로 등록해야 한다.
* */
