package matal.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;



@NoArgsConstructor
@Entity
@Getter
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long member_id;

    @Column(nullable = false)
    private String pwd;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "생년월일은 yyyyMMdd 형식이어야 합니다.")
    @Column
    private String birth;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime last_login;
    public void addUserAuthority() {
        this.role = Role.USER;
    }
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.pwd = passwordEncoder.encode(pwd);
    }

    @Builder
    public Member(String name, String pwd, String email, String birth, Role role) {
        this.name = name;
        this.pwd=pwd;
        this.email = email;
        this.birth=birth;
        this.role=role;
    }
}