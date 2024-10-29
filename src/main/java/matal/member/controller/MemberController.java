package matal.member.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matal.global.exception.ErrorResponse;
import matal.member.dto.request.LoginRequestDto;
import matal.member.dto.request.SignUpRequestDto;
import matal.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "user", description = "SignUp API")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입 기능", description = "사용자가 회원가입을 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 회원 정보",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        memberService.signUp(signUpRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 기능", description = "사용자가 로그인 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 정보",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        memberService.login(loginRequestDto, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 기능", description = "사용자가 로그하웃 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공")})
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        memberService.logout(session);
        return ResponseEntity.ok().build();
    }
}
