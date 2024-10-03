package matal.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matal.jwt.repository.BlackListRepository;
import matal.member.dto.LoginResponse;
import matal.member.dto.MemberLoginDto;
import matal.member.dto.MemberSignUpRequestDto;
import matal.member.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    
    private final MemberService memberService;
    
    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "사용자의 정보를 저장하는 API")
    public Long register(@Valid @RequestBody MemberSignUpRequestDto memberSignUpRequestDto) //회원 id 리턴
            throws Exception {
        return memberService.signUp(memberSignUpRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자의 로그인을 진행하며 jwt 토큰을 발급받는 API")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto loginDto) {
        try {
            // 서비스의 login 메서드를 호출하여 JWT 토큰을 받아옴
            LoginResponse response = memberService.login(loginDto);

            // 응답으로 메시지와 JWT 토큰을 전달
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 로그인 실패 (잘못된 이메일 또는 비밀번호)
            logger.warn("로그인 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            // 기타 예외 발생
            logger.error("로그인 중 예기치 않은 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"서버 오류가 발생했습니다.\"}");
        }
    }
    
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자의 정보를 저장하는 API")
    public ResponseEntity<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) throws Exception {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
        }
        // Authorization 헤더에서 토큰 추출
        String token = authorizationHeader.replace("Bearer ", "");

        // 로그아웃 서비스 메서드 호출
        memberService.logout(token);
        // 성공 응답 반환
        return ResponseEntity.ok("Logged out successfully.");
   }
}


