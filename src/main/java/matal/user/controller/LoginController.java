package matal.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import matal.user.dto.request.SignUpRequestDto;
import matal.user.service.CustomUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "user", description = "SignUp API")
public class LoginController {

    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입 기능", description = "사용자가 회원가입을 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = Boolean.class))}),
            @ApiResponse(responseCode = "404", description = "실패")})
    public Boolean signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        return customUserDetailsService.signUp(signUpRequestDto);
    }
}
