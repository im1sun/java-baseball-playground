package login.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import login.app.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import login.app.MemberRepository;
//import login.domain.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<List<Member>> getMembers(HttpServletRequest request) {
        // 사용자 인증 정보 추출
        String[] credentials = extractBasicAuthCredentials(request);
        if (credentials == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = credentials[0];
        String password = credentials[1];

        // 사용자 인증 검증
        Member authenticatedMember = memberRepository.findByEmail(email).orElse(null);
        if(authenticatedMember == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 회원 목록 조회
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
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
