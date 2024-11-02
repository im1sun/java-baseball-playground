package study;

import login.LoginServiceApplication;
import login.app.Member;
import login.app.MemberRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

        memberRepository.save(TEST_MEMBER);
        ResultActions loginResponse = mockMvc.perform(
                post("/login")
                        .param("username", TEST_MEMBER.getEmail())
                        .param("password", TEST_MEMBER.getPassword())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
        System.out.println("loginResponse = " + loginResponse);
        loginResponse.andDo(print());
        loginResponse.andExpect(status().isOk());
        HttpSession session = loginResponse.andReturn().getRequest().getSession();
        assertThat(session).isNotNull();
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isEqualTo(TEST_MEMBER);
    }

    @DisplayName("로그인 실패 - 사용자 없음")
    @Test
    void username_fail() throws Exception {
        memberRepository.save(TEST_MEMBER);
        ResultActions loginResponse = mockMvc.perform(
                post("/login")
                        .param("username", "emuceeee")
                        .param("password", "test1234")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
        loginResponse.andExpect(status().isOk());
        HttpSession session = loginResponse.andReturn().getRequest().getSession();
        assertThat(session).isNotNull();
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNull();
    }

    @DisplayName("로그인 실패 - 비밀번호 불일치")
    @Test
    void password_fail() throws Exception {
        memberRepository.save(TEST_MEMBER);
        ResultActions loginResponse = mockMvc.perform(
                post("/login")
                        .param("username", TEST_MEMBER.getEmail())
                        .param("password", "wrongpassword")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
        loginResponse.andExpect(status().isOk());
        HttpSession session = loginResponse.andReturn().getRequest().getSession();
        assertThat(session).isNotNull();
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNull();
}

}
