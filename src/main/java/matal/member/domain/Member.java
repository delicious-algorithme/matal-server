package matal.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Boolean serviceAgreement;

    @Column(nullable = false)
    private Boolean privacyAgreement;

    @Column(nullable = false)
    private Boolean ageConfirmation;

    @Builder
    public Member(
            String email,
            String password,
            String nickname,
            Role role,
            Boolean serviceAgreement,
            Boolean privacyAgreement,
            Boolean ageConfirmation
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.serviceAgreement = serviceAgreement;
        this.privacyAgreement = privacyAgreement;
        this.ageConfirmation = ageConfirmation;
    }
}
