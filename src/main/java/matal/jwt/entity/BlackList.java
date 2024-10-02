package matal.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor
@NoArgsConstructor()
@Getter
@Entity
public class BlackList {
    //로그아웃한 refresh token을 저장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invalid_refresh_token")
    private String invalidRefreshToken;

    public BlackList(String token) {
        this.invalidRefreshToken=token;
    }
}
