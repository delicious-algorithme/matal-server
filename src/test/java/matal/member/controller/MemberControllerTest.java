package matal.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import matal.member.domain.Role;
import matal.member.domain.Member;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.LoginRequestDto;
import matal.member.dto.request.SignUpRequestDto;
import matal.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
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

        Member member1 = Member.builder()
                .email("login@test.com")
                .password(passwordEncoder.encode("login"))
                .nickname("login")
                .role(Role.MEMBER)
                .build();

        memberRepository.save(member1);
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testSignUpSuccess() throws Exception {
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
    @DisplayName("회원가입 시 비어있는 이메일 값으로 인한 실패 테스트")
    public void testSignUpFailure() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(null, "test", "test");

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signUpRequestDto);

        //then
        mockMvc.perform(post("/api/users/signup")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 시 비어있는 비밀번호 값으로 인한 실패 테스트")
    public void testSignUpFailure2() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto("test@test.com", null, "test");

        //when
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signUpRequestDto);

        //then
        mockMvc.perform(post("/api/users/signup")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLoginSuccess() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@test.com", "test");

        //when
        doAnswer(invocationOnMock -> {
            session = invocationOnMock.getArgument(1);
            session.setAttribute("member", loginRequestDto.email());
            return null;
        }).when(memberService).login(any(), any());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        //then
        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());

        session = (MockHttpSession) resultActions.andReturn().getRequest().getSession();
        String mockEmail = (String) session.getAttribute("member");
        assertThat(session).isNotNull();
        assertThat(mockEmail).isEqualTo(loginRequestDto.email());
    }

    @Test
    @DisplayName("로그인 시 비어있는 이메일 값으로 인한 실패 테스트")
    public void testLoginFailure() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto(null, "test");

        //when
        doAnswer(invocationOnMock -> {
            session = invocationOnMock.getArgument(1);
            session.setAttribute("member", null);
            return null;
        }).when(memberService).login(any(), any());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        //then
        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 비어있는 비밀번호 값으로 인한 실패 테스트")
    public void testLoginFailure2() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@test.com", null);

        //when
        doAnswer(invocationOnMock -> {
            session = invocationOnMock.getArgument(1);
            session.setAttribute("member", null);
            return null;
        }).when(memberService).login(any(), any());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        //then
        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 시 올바르지 않은 이메일 형식으로 인한 실패 테스트")
    public void testLoginFailure3() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test", "test");

        //when
        doAnswer(invocationOnMock -> {
            session = invocationOnMock.getArgument(1);
            session.setAttribute("member", null);
            return null;
        }).when(memberService).login(any(), any());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        //then
        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    public void testLogout() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(post("/api/users/logout")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
