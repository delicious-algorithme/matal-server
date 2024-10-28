package matal.member.dto.request;

public record SignUpRequestDto(
        String email,
        String password,
        String nickname
) {
}
