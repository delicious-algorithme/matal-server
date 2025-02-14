package matal.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import matal.member.dto.request.LoginRequestDto;
import matal.member.dto.request.SignUpRequestDto;
import matal.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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

    private MockHttpSession session;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSignUpSuccess() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                "test@test.com",
                "test",
                "test",
                true,
                true,
                true
        );

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
    void testSignUpFailure() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                null,
                "test",
                "test",
                true,
                true,
                true
        );

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
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                "test@test.com",
                null,
                "test",
                true,
                true,
                true
        );

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
    void testLoginSuccess() throws Exception {
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
    void testLoginFailure() throws Exception {
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
    void testLoginFailure2() throws Exception {
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
    void testLoginFailure3() throws Exception {
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
    void testLogout() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(post("/api/users/logout")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
