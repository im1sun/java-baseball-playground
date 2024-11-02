package login.security;

import login.app.LoginService;
import login.app.Member;
import login.app.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationInterceptor implements HandlerInterceptor {

    private final LoginService loginService;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("BasicAuthenticationInterceptor.preHandle");

        // 사용자 인증 정보 추출
        String[] credentials = extractBasicAuthCredentials(request);
        if (credentials == null) {
            return false;
        }

        String email = credentials[0];
        String password = credentials[1];

        // 사용자 인증 검증
        Member loginMember = loginService.login(email, password);
        if(loginMember == null) {
            return false;
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        System.out.println("session = " + session);
        //세션에 로그인 회원 정보 보관
        session.setAttribute("SPRING_SECURITY_CONTEXT", loginMember);

        // 회원 목록 조회
        List<Member> members = memberRepository.findAll();
        System.out.println("members = " + members);
        return true;
    }

    private String[] extractBasicAuthCredentials(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length());
            String credentials = new String(org.springframework.util.Base64Utils.decodeFromString(base64Credentials));
            System.out.println("credentials = " + credentials);
            return credentials.split(":", 2);
        }
        return null;
    }

}
