package com.web.controller;

import com.web.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
// API URI 경로를 "/board"로 정의
@RequestMapping("/board")
public class BoardController {

//    boardService 의존성을 주입해야 하므로 @Autowired를 사용한다.
    @Autowired
    BoardService boardService;

//    매핑 경로를 중괄호{}를 사용하여 여러 개를 받을 수 있다.
    @GetMapping({"", "/"})
//  @RequestParam 어노테이션을 사용하여 idx 파라미터를 필수로 받는다.
// 만약, 바인딩할 값이 없으면 defaultValue에 준 값인 기본값 '0'으로 설정된다.
// findBoardByIdx(idx)로 조회 시 idx값을 '0'으로 조회하면 board값은 null값으로 반환된다.
    public String board(@RequestParam(value = "idx", defaultValue = "0") Long idx,
                        Model model) {
        model.addAttribute("board", boardService.findBoardByIdx(idx));
        return  "/board/form";
    }

    @GetMapping("/list")
    public String list(@PageableDefault Pageable pageable, Model model) {
//        @PageableDefault 어노테이션의 파라미터인 size, sort, direction 등을 사용하여
//        페이징에 대한 규약을 정의할 수 있다.
        model.addAttribute("boardList", boardService.findBoardList(pageable));
//        src/resources/templates를 기준으로 데이터를 바인딩할 타깃의 뷰 경로를 지정한다.
        return "/board/list";
    }
}
