package login.controller;

import login.domain.LoginService;
import login.domain.Member;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IdPasswordController {

    private final LoginService loginService;

    @PostMapping("/login")
    public String loginIdPassword(@RequestParam("username") String email,
                          @RequestParam("password") String password,
                          HttpServletRequest request) {

        Member loginMember = loginService.login(email, password);
        System.out.println("loginMember = " + loginMember);

        if (loginMember == null) {
            return "UNAUTHORIZED";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        System.out.println("session = " + session);
        //세션에 로그인 회원 정보 보관
        session.setAttribute("SPRING_SECURITY_CONTEXT", loginMember);

        return "OK";

    }
}
