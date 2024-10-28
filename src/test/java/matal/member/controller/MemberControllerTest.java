package matal.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import matal.member.domain.Role;
import matal.member.domain.Member;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.SignUpRequestDto;
import matal.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
@ActiveProfiles("local")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        session = new MockHttpSession();

        Member member1 = Member.builder()
                .email("login@test.com")
                .password(passwordEncoder.encode("login"))
                .nickname("login")
                .role(Role.MEMBER)
                .build();

        memberRepository.save(member1);
    }

    @Test
    public void testSignUp() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("request@test.com", "test", "test");

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signUpRequestDto);

        //then
        mockMvc.perform(post("/api/users/signup")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testSignUpFailure() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("login@test.com", "test", "test");

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signUpRequestDto);

        //then
        mockMvc.perform(post("/api/users/signup")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andDo(print());
    }
}
