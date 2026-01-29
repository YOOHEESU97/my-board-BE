package my_board.board.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey key = Keys.hmacShaKeyFor(
            "my-very-secret-key-must-be-32-bytes-long!".getBytes(StandardCharsets.UTF_8)
    );
    private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60;

    // ✅ 토큰 생성 3600000
    public String createToken(String email, String role, String nickname) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .claim("nickname", nickname)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + ACCESS_TOKEN_VALIDITY))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7 ))
                .signWith(key)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key) // 서명 키 설정
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getNickname(String token) {
        try{
        return Jwts.parser()
                .verifyWith(key) // 서명 키 설정
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("nickname", String.class);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("nickname", String.class);
        }
    }

    public String getRole(String token) {
        try{
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);
        } catch (ExpiredJwtException e) {
            return e.getClaims().get("role", String.class);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("토큰 검증 실패: 만료됨 - " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("토큰 검증 실패: 기타 JWT 예외 - " + e.getClass().getSimpleName() + " - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("토큰 검증 실패: 잘못된 인자 - " + e.getMessage());
        }
        return false;
    }
}