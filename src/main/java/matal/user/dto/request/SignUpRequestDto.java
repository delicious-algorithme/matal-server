package matal.user.dto.request;

public record SignUpRequestDto(
        String email,
        String pwd,
        String pwdChek,
        String nickname
) {
}
