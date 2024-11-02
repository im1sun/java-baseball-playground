package study;

import login.LoginServiceApplication;
import login.app.Member;
import login.app.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LoginServiceApplication.class)
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    Member TEST_MEMBER = new Member(1L, "emuce@nate.com", "test1234");
    Member TEST_MEMBER2 = new Member(2L, "emuce@naver.com", "test1234");
    Member TEST_MEMBER3 = new Member(3L, "emuce@gmail.com", "test1234");

    @BeforeEach
    void setUp() {
        // 테스트용 회원 데이터 저장
        memberRepository.save(TEST_MEMBER);
        memberRepository.save(TEST_MEMBER2);
        memberRepository.save(TEST_MEMBER3);
    }


    @DisplayName("Basic Auth 인증 성공 후 회원 목록 조회")
    @Test
    void members() throws Exception {
        String token = TEST_MEMBER.getEmail() + ":" + TEST_MEMBER.getPassword();
        String encoded = Base64Utils.encodeToString(token.getBytes());

        ResultActions loginResponse = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + encoded)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
        loginResponse.andDo(print());
        loginResponse.andExpect(status().isOk());
        loginResponse.andExpect(jsonPath("$[*].email", hasItem(TEST_MEMBER.getEmail())));
        HttpSession session = loginResponse.andReturn().getRequest().getSession();
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isEqualTo(TEST_MEMBER);
    }

    @DisplayName("Basic Auth 인증 실패 시 에러 응답")
    @Test
    void membersFail() throws Exception {
        String token = "test@naver.com" + ":" + TEST_MEMBER.getPassword();
        String encoded = Base64Utils.encodeToString(token.getBytes());

        ResultActions loginResponse = mockMvc.perform(get("/members")
                .header("Authorization", "Basic " + encoded)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        );
        loginResponse.andDo(print());
        loginResponse.andExpect(status().isOk());
        HttpSession session = loginResponse.andReturn().getRequest().getSession();
        assertThat(session.getAttribute("SPRING_SECURITY_CONTEXT")).isNull();
    }

}
