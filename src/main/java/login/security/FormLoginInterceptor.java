package login.security;

import login.app.LoginService;
import login.app.Member;
import login.app.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@RequiredArgsConstructor
public class FormLoginInterceptor implements HandlerInterceptor {

    private final LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("FormLoginInterceptor.preHandle");
        System.out.println("request.getParameter(\"username\") = " + request.getParameter("username"));
        System.out.println("request.getParameter(\"password\") = " + request.getParameter("password"));
                

        Member loginMember = loginService.login(request.getParameter("username"), request.getParameter("password"));

        if (loginMember == null) {
            System.out.println("로그인에 실패하였습니다.");
            return false;
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        System.out.println("session = " + session);
        //세션에 로그인 회원 정보 보관
        session.setAttribute("SPRING_SECURITY_CONTEXT", loginMember);

        return true;
    }

}
