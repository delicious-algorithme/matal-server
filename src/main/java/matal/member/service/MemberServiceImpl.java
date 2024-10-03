package matal.member.service;

import lombok.RequiredArgsConstructor;
import matal.jwt.JwtUtil;
import matal.jwt.entity.BlackList;
import matal.jwt.repository.BlackListRepository;
import matal.member.dto.LoginResponse;
import matal.member.dto.MemberLoginDto;
import matal.member.dto.MemberSignUpRequestDto;
import matal.member.entity.Member;
import matal.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    
    private final BlackListRepository blackListRepository;
    
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secretKey}")
    private String secretKey;
    
    @Value("${jwt.access.expiration}")
    private Long accessExpiredMs;  

    @Override
    public LoginResponse login(MemberLoginDto requestDto) throws Exception {
        Member member = memberRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        if(!passwordEncoder.matches(requestDto.getPwd(), member.getPwd())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        // 엑세스 토큰 생성
        String accessToken = JwtUtil.createAccessToken(member.getMember_id(), secretKey, accessExpiredMs);

        // 엑세스 토큰과 리프레시 토큰을 응답으로 반환
        return new LoginResponse("Log in Complete", accessToken);
    }
    @Override
    public void logout(String token) {
        // JWT 토큰이 유효하지 않은 경우 처리
        if (JwtUtil.isExpired(token, secretKey)) {
            throw new IllegalArgumentException("유효하지 않은 또는 만료된 토큰입니다.");
        }

        // 이미 블랙리스트에 등록된 토큰인지 확인
        if (blackListRepository.existsByInvalidRefreshToken(token)) {
            throw new IllegalArgumentException("이미 로그아웃된 토큰입니다.");
        }
        BlackList blackList = new BlackList(token);
        blackListRepository.save(blackList);
    }

    @Override
    public Long signUp(MemberSignUpRequestDto requestDto) throws Exception {
        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if(!requestDto.getPwd().equals(requestDto.getCheckpwd())){
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPwd());
        requestDto.setPwd(encodedPassword); 
        
        Member member = memberRepository.save(requestDto.toEntity());
        member.addUserAuthority();
        return member.getMember_id();
        
    }
}


