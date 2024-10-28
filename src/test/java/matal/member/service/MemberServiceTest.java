package matal.member.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import matal.member.domain.Role;
import matal.member.domain.Member;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class MemberServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MemberRepository memberRepository;

    @Mock
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
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testLogin() throws Exception {
        //given
        String testId = "login@test.com";
        String testPwd = "login";

        //when & then
        mockMvc.perform(post("/login")
                        .param("username", testId)
                        .param("password", testPwd))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());
    }
}
