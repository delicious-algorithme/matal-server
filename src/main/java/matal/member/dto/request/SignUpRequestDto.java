package matal.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequestDto(
        @Email @NotBlank String email,
        @NotBlank String password,
        String nickname,
        Boolean serviceAgreement,
        Boolean privacyAgreement,
        Boolean ageConfirmation
) {

}
