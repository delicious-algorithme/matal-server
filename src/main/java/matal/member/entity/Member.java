package matal.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime last_login;
    
    public void addUserAuthority() {
        this.role = Role.USER;
    }

    @Builder
    public Member(String name, String password, String email, Role role) {
        this.name = name;
        this.password=password;
        this.email = email;
        this.role=role;
    }
}
