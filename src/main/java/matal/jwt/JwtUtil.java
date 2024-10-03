package matal.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    public static Long getId(String token, String secretKey){
        // secretKey를 이용해 Key 객체 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 파서 빌더에 Key 객체를 전달
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long res = claims.get("id", Long.class);
        System.out.println("res : " + res);
        return res;
    }

    public static boolean isExpired(String token, String secretKey){
        // secretKey를 이용해 Key 객체 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 토큰의 만료 여부 확인
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }

    // 엑세스 토큰 생성
    public static String createAccessToken(Long id, String secretKey, Long expiredMs) {
        return createJwt(id, secretKey, expiredMs);
    }

    public static String createJwt(Long id, String secretKey, Long expiredMs){
        // secretKey를 이용해 Key 객체 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // 클레임 설정
        Claims claims = Jwts.claims();
        claims.put("id", id);

        // 토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
