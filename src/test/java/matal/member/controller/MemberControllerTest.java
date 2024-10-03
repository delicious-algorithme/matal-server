package matal.member.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import matal.config.SecurityConfig;
import matal.jwt.JwtUtil;
import matal.member.dto.LoginResponse;
import matal.member.dto.MemberLoginDto;
import matal.member.dto.MemberSignUpRequestDto;
import matal.jwt.repository.BlackListRepository;
import matal.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@Import({SecurityConfig.class})
@ActiveProfiles("local")
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BlackListRepository blackListRepository;  // JwtRepository 모킹 추가

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 기능 테스트")
    public void testMemberSignUp() throws Exception {
        // Given
        MemberSignUpRequestDto requestDto = MemberSignUpRequestDto.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .password("password")
                .checkpwd("password")
                .build();

        // 회원가입 성공 시 반환될 회원 ID
        Long memberId = 1L;

        // When
        when(memberService.signUp(any(MemberSignUpRequestDto.class))).thenReturn(memberId);

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("응답 메시지: " + responseContent);
        // Service의 signUp 메서드가 한 번 호출되었는지 검증
        verify(memberService, times(1)).signUp(any(MemberSignUpRequestDto.class));
    }
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.access.expiration}")
    private Long accessExpiredMs;  // 엑세스 토큰 만료 시간

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLoginSuccess() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "password";
        MemberLoginDto loginDto = new MemberLoginDto(email, password);

        String accessToken = JwtUtil.createAccessToken(1L, secretKey, accessExpiredMs);

        // 서비스에서 반환할 LoginResponse 객체를 미리 정의
        LoginResponse loginResponse = new LoginResponse("Log in Complete", accessToken);

        // When
        when(memberService.login(any(MemberLoginDto.class))).thenReturn(loginResponse);

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 응답에서 토큰 추출
        String responseContent = mvcResult.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(responseContent, LoginResponse.class);

        // 로그인 후 발급된 accessToken을 헤더에 추가하여 다른 요청 테스트
        mockMvc.perform(post("/api/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccessToken()))
                .andExpect(status().isOk());

        // 토큰 검증
        assertEquals(loginResponse.getAccessToken(), response.getAccessToken());

        // 서비스 메서드가 한 번 호출되었는지 검증
        verify(memberService, times(1)).login(any(MemberLoginDto.class));
    }

    @Test
    @DisplayName("잘못된 이메일로 로그인 실패 테스트")
    public void testLoginFailure_InvalidEmail() throws Exception {
        // Given
        String email = "wrong@example.com";
        String password = "password";
        MemberLoginDto loginDto = new MemberLoginDto(email, password);

        // When - 이메일이 잘못된 경우 IllegalArgumentException 발생
        when(memberService.login(any(MemberLoginDto.class)))
                .thenThrow(new IllegalArgumentException("가입되지 않은 이메일 입니다."));

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("가입되지 않은 이메일 입니다."));

        verify(memberService, times(1)).login(any(MemberLoginDto.class));
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 실패 테스트")
    public void testLoginFailure_InvalidPassword() throws Exception {
        // Given
        String email = "test@example.com";
        String wrongPassword = "wrongpassword";
        MemberLoginDto loginDto = new MemberLoginDto(email, wrongPassword);

        // When - 비밀번호가 잘못된 경우 IllegalArgumentException 발생
        when(memberService.login(any(MemberLoginDto.class)))
                .thenThrow(new IllegalArgumentException("잘못된 비밀번호 입니다."));

        // Then
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("잘못된 비밀번호 입니다."));

        verify(memberService, times(1)).login(any(MemberLoginDto.class));
    }
    @Test
    @DisplayName("로그인 성공 후 로그아웃 테스트")
    public void testLoginAndLogout() throws Exception {
        // Given
        String email = "test@example.com";
        String password = "password";
        MemberLoginDto loginDto = new MemberLoginDto(email, password);

        Long memberId = 1L;
        String accessToken = JwtUtil.createAccessToken(memberId, secretKey, accessExpiredMs);

        // 로그인 응답 모킹
        LoginResponse loginResponse = new LoginResponse("Log in Complete", accessToken);
        when(memberService.login(any(MemberLoginDto.class))).thenReturn(loginResponse);

        // When - 로그인 요청
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 응답에서 토큰 추출
        String loginResponseContent = loginResult.getResponse().getContentAsString();
        LoginResponse response = objectMapper.readValue(loginResponseContent, LoginResponse.class);

        // Then - 로그아웃 요청
        doNothing().when(memberService).logout(any(String.class));
        mockMvc.perform(post("/api/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully."));

        // 서비스 메서드 호출 검증
        verify(memberService, times(1)).login(any(MemberLoginDto.class));
        verify(memberService, times(1)).logout(any(String.class));
    }
    @Test
    @DisplayName("토큰 없이 로그아웃 실패 테스트")
    public void testLogoutFailure_MissingToken() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Authorization 헤더가 없거나 형식이 올바르지 않습니다."));
    }




}
