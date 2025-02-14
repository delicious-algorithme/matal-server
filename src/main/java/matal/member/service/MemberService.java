package matal.member.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import matal.global.exception.AlreadyExistException;
import matal.global.exception.AuthException;
import matal.global.exception.NotFoundException;
import matal.global.exception.ResponseCode;
import matal.member.domain.Member;
import matal.member.domain.Role;
import matal.member.domain.repository.MemberRepository;
import matal.member.dto.request.AuthMember;
import matal.member.dto.request.LoginRequestDto;
import matal.member.dto.request.SignUpRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final String SESSION_KEY = "member";

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequestDto signUpRequestDto) {

        if (memberRepository.findByEmail(signUpRequestDto.email()).isPresent()) {
            throw new AlreadyExistException(ResponseCode.MEMBER_ALREADY_EXIST_EXCEPTION);
        }

        if (memberRepository.findByNickname(signUpRequestDto.nickname()).isPresent()) {
            throw new AlreadyExistException(ResponseCode.MEMBER_NICKNAME_ALREADY_EXIST_EXCEPTION);
        }

        if (!signUpRequestDto.serviceAgreement()
                || !signUpRequestDto.privacyAgreement()
                || !signUpRequestDto.ageConfirmation()) {
            throw new AlreadyExistException(ResponseCode.MEMBER_AGREEMENT_NOT_ACCEPTED);
        }

        saveMember(signUpRequestDto);
    }

    @Transactional(readOnly = true)
    public void login(LoginRequestDto loginRequestDto, HttpSession session) {

        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new NotFoundException(ResponseCode.MEMBER_NOT_FOUND_EMAIL));

        if (!passwordEncoder.matches(loginRequestDto.password(), member.getPassword())) {
            throw new AuthException(ResponseCode.MEMBER_AUTH_EXCEPTION);
        }

        session.setAttribute(SESSION_KEY, AuthMember.from(member));
    }

    @Transactional
    public void logout(HttpSession session) {

        if (session != null) {
            session.invalidate();
        }
    }

    public void saveMember(SignUpRequestDto signUpRequestDto) {

        Member newMember = Member.builder()
                .email(signUpRequestDto.email())
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .nickname(signUpRequestDto.nickname())
                .role(Role.MEMBER)
                .privacyAgreement(signUpRequestDto.privacyAgreement())
                .serviceAgreement(signUpRequestDto.serviceAgreement())
                .ageConfirmation(signUpRequestDto.ageConfirmation())
                .build();

        memberRepository.save(newMember);
    }
}
