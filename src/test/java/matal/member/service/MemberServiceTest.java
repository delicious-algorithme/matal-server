package matal.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import matal.global.exception.AlreadyExistException;
import matal.global.exception.AuthException;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.member.domain.Member;
import matal.member.domain.Role;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.LoginRequestDto;
import matal.member.dto.request.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void beforeEach() {
        member = Member.builder()
                .email("test@test.com")
                .password("test")
                .nickname("test")
                .role(Role.MEMBER)
                .build();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void testSignUp() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                "test@test.com",
                "test",
                "test"
        );

        //when
        when(memberRepository.findByEmail(signUpRequestDto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signUpRequestDto.password())).thenReturn("encryptedTestPassword");

        //then
        assertThatCode(
                () -> memberService.signUp(signUpRequestDto)).doesNotThrowAnyException();
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 회원으로 인한 실패 테스트")
    void testSignUpFailure() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto(
                "request@test.com",
                "test",
                "test"
        );

        //when
        when(memberRepository.findByEmail(anyString()))
                .thenThrow(new AlreadyExistException(ResponseCode.MEMBER_ALREADY_EXIST_EXCEPTION));

        //then
        assertThrows(AlreadyExistException.class, () -> memberService.signUp(signUpRequestDto));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void testLoginSuccess() {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@test.com", "test");
        MockHttpSession session = new MockHttpSession();

        // when
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // then
        memberService.login(loginRequestDto, session);
        String sessionEmail = (String) session.getAttribute("member");
        assertThat(sessionEmail).isEqualTo(loginRequestDto.email());
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 회원 정보로 인한 실패 테스트")
    void testLoginFailure() {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@test.com", "test");

        // when
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> memberService.login(loginRequestDto, any()));
    }

    @Test
    @DisplayName("로그인 시 일치하지 않은 비밀번호로 인한 실패 테스트")
    void testLoginFailure2() {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@test.com", "test");

        // when
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // then
        assertThrows(AuthException.class, () -> memberService.login(loginRequestDto, any()));
    }

    @Test
    @DisplayName("로그아웃 성공 테스트")
    void testLogout() {
        // given
        MockHttpSession session = new MockHttpSession();

        // when

        // then
        memberService.logout(session);
        assertThat(session.isInvalid()).isEqualTo(true);

    }
}
