package com.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @ResponseBody를 모든 메소드에서 적용해준다.
@RestController
public class WebRestController {
    // hello 메소드의 결과로 "HelloWorld"라는 문자열을 JSON 형태로 반환한다.
    @GetMapping("/hello")
    public String hello() {
        return "HelloWorld";
    }
}
