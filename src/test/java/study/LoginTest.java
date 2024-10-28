package study;

import login.LoginServiceApplication;
import login.domain.LoginService;
import login.domain.Member;
import login.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LoginServiceApplication.class)
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    Member TEST_MEMBER = new Member(1L, "emuce@nate.com", "test1234");


    @DisplayName("로그인 성공")
    @Test
    void login_success() throws Exception {
        System.out.println("LoginTest.login_success1111111111");
        memberRepository.save(TEST_MEMBER);
        ResultActions loginResponse = mockMvc.perform(
                post("/login")
                        .param("username", TEST_MEMBER.getEmail())
                        .param("password", TEST_MEMBER.getPassword())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
        System.out.println("loginResponse = " + loginResponse);
        System.out.println("loginResponse = " + loginResponse.andExpect(status().isOk()));
        loginResponse.andExpect(status().isOk());
        System.out.println("loginResponse = " + loginResponse.andReturn().getRequest().getSession());
        HttpSession session = loginResponse.andReturn().getRequest().getSession();
        assertThat(session).isNotNull();
        System.out.println("session.getAttribute(\"SPRING_SECURITY_CONTEXT\") = " + session.getAttribute("SPRING_SECURITY_CONTEXT"));
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isEqualTo(TEST_MEMBER);
    }
//    @DisplayName("로그인 실패 - 사용자 없음")
//// ...
//    @DisplayName("로그인 실패 - 비밀번호 불일치")
//// ...

}
