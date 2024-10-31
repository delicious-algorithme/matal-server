package matal.member.dto.request;

import matal.member.domain.Member;
import matal.member.domain.Role;

public record AuthMember(Long memberId,
                         String email,
                         String nickname,
                         Role role) {

    public static AuthMember from(Member member) {
        return new AuthMember(
                member.getMemberId(),
                member.getEmail(),
                member.getNickname(),
                member.getRole()
        );
    }
}
