package com.web.controller;

import com.web.annotation.SocialUser;
import com.web.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 인증된 User 정보를 세션에 저장해주는 기능 생성
@Controller
public class LoginController {

    @GetMapping ("/login")
    public String login() {
        return "login";
    }

/*
인증이 성공적으로 처리된 이후에 리다이렉트되는 경로
허용하는 요청의 URL 매핑을 /facebook/complete, /google/complete, /kakao/complete로 제한한다.
*/
    @GetMapping (value="/{facebook|google|kakao}/complete")
    /*
    @SocialUser User user 형식의 간단한 방법으로 인증된 User 객체를 가져올 수 있다.
    -> 코드의 재사용성에 있어서도 큰 이득을 얻게 된다.
    단지, 컨트롤러의 파라미터에 @SocialUser 어노테이션이 있고 타입이 User면 된다.
     */
    public String loginComplete(@SocialUser User user) {
        return "redirect:/board/list";
    }
}
