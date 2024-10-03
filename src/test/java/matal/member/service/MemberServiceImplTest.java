package matal.member.service;

import matal.config.SecurityConfig;
import matal.jwt.JwtUtil;
import matal.jwt.repository.BlackListRepository;
import matal.member.controller.MemberController;
import matal.member.dto.LoginResponse;
import matal.member.dto.MemberLoginDto;
import matal.member.dto.MemberSignUpRequestDto;
import matal.member.entity.Member;
import matal.member.entity.Role;
import matal.member.repository.MemberRepository;
import matal.member.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import({SecurityConfig.class})
@ActiveProfiles("local")
public class MemberServiceImplTest {
    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BlackListRepository blackListRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // MemberServiceImpl에 secretKey 값을 직접 주입합니다.
        memberService = new MemberServiceImpl(memberRepository, blackListRepository, passwordEncoder);
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    public void testLogin_Failure_WrongPassword() {
        // Given
        MemberLoginDto loginDto = new MemberLoginDto("test@test.com", "wrongpassword");
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword("encodedPassword");

        when(memberRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(loginDto.getPassword(), member.getPassword())).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.login(loginDto);
        });
        assertEquals("잘못된 비밀번호 입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    public void testLogin_Failure_EmailNotFound() {
        // Given
        MemberLoginDto loginDto = new MemberLoginDto("nonexistent@test.com", "password");

        when(memberRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.login(loginDto);
        });
        assertEquals("가입되지 않은 이메일 입니다.", exception.getMessage());
    }

    // 회원가입 테스트
    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testSignUp_Success() throws Exception {
        // Given: 회원가입 요청 DTO 설정
        MemberSignUpRequestDto signUpDto = MemberSignUpRequestDto.builder()
                .email("test@test.com")
                .name("John Doe")
                .password("password")
                .checkpwd("password")
                .role(Role.USER)
                .build();

        // 엔티티 변환된 Member 객체
        Member member = signUpDto.toEntity();
        member.setId(1L); // Member 엔티티 ID 설정

        // Mock 설정: 이메일이 중복되지 않고 비밀번호가 인코딩되어 저장됨
        when(memberRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // When: 회원가입 실행
        Long memberId = memberService.signUp(signUpDto);

        // Then: 회원 ID가 반환되고, 비밀번호가 인코딩되며 저장되는지 확인
        assertEquals(1L, memberId);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 이메일")
    public void testSignUp_Failure_DuplicateEmail() throws Exception {
        // Given: 중복된 이메일의 회원가입 DTO 설정
        MemberSignUpRequestDto signUpDto = MemberSignUpRequestDto.builder()
                .email("test@test.com")
                .name("John Doe")
                .password("password")
                .checkpwd("password")
                .role(Role.USER)
                .build();

        // Mock 설정: 이메일이 이미 존재하는 경우
        when(memberRepository.findByEmail(signUpDto.getEmail())).thenReturn(Optional.of(new Member()));

        // When & Then: 회원가입 시 예외 발생 확인
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.signUp(signUpDto);
        });

        assertEquals("이미 존재하는 이메일입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("회원가입 실패 - 비밀번호 불일치")
    public void testSignUp_Failure_PasswordMismatch() throws Exception {
        // Given: 비밀번호와 확인 비밀번호가 다른 회원가입 DTO 설정
        MemberSignUpRequestDto signUpDto = MemberSignUpRequestDto.builder()
                .email("test@test.com")
                .name("John Doe")
                .password("password")
                .checkpwd("differentPassword")
                .role(Role.USER)
                .build();

        // When & Then: 회원가입 시 비밀번호 불일치 예외 발생 확인
        Exception exception = assertThrows(Exception.class, () -> {
            memberService.signUp(signUpDto);
        });

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }
}

