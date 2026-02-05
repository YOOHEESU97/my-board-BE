package my_board.board.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT(JSON Web Token) 토큰 생성 및 검증
 * - Access Token 및 Refresh Token 생성
 * - 토큰에서 사용자 정보(이메일, 닉네임, 권한) 추출
 * - 토큰 유효성 검증
 */
@Component
public class JwtTokenProvider {
    /**
     * JWT 서명에 사용되는 비밀 키
     * application.yml에 선언 KEY 명 바꿔야함
     */
    private final SecretKey key = Keys.hmacShaKeyFor(
            "my-very-secret-key-must-be-32-bytes-long!".getBytes(StandardCharsets.UTF_8)
    );

    private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60;

    /**
     * Access Token 생성
     * 사용자 인증 및 API 접근에 사용되는 단기 유효 토큰 (1시간)
     * 
     * @param email    사용자 이메일 (subject로 저장)
     * @param role     사용자 권한 (예: ROLE_USER, ROLE_ADMIN)
     * @param nickname 사용자 닉네임
     * @return 생성된 JWT Access Token 문자열
     */
    public String createToken(String email, String role, String nickname) {
        return Jwts.builder()
                .subject(email)                 // 토큰 제목(주체): 사용자 이메일
                .claim("role", role)           // 사용자 권한 정보
                .claim("nickname", nickname)   // 사용자 닉네임
                .issuedAt(new Date())          // 토큰 발급 시간
                .expiration(new Date((new Date()).getTime() + ACCESS_TOKEN_VALIDITY))  // 만료 시간
                .signWith(key)                 // 서명 키로 토큰 서명
                .compact();                    // JWT 문자열로 압축
    }

    /**
     * Refresh Token 생성
     * Access Token 재발급에 사용되는 장기 유효 토큰 (7일)
     * 
     * @param email 사용자 이메일
     * @return 생성된 JWT Refresh Token 문자열
     */
    public String createRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7))  // 7일
                .signWith(key)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이메일 추출
     * 
     * @param token JWT 토큰 문자열
     * @return 토큰의 subject에 저장된 사용자 이메일
     */
    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(key)              // 서명 키로 검증
                .build()
                .parseSignedClaims(token)     // 토큰 파싱
                .getPayload()
                .getSubject();                // 이메일 반환
    }

    /**
     * JWT 토큰에서 사용자 닉네임 추출
     * 토큰이 만료된 경우에도 닉네임을 추출할 수 있도록 예외 처리
     * 
     * @param token JWT 토큰 문자열
     * @return 토큰의 claims에 저장된 닉네임
     */
    public String getNickname(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("nickname", String.class);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었어도 claims에서 닉네임 추출 (재발급 시 필요)
            return e.getClaims().get("nickname", String.class);
        }
    }

    /**
     * JWT 토큰에서 사용자 권한(role) 추출
     * 토큰이 만료된 경우에도 권한을 추출할 수 있도록 예외 처리
     * 
     * @param token JWT 토큰 문자열
     * @return 토큰의 claims에 저장된 권한 (예: ROLE_USER)
     */
    public String getRole(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("role", String.class);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었어도 claims에서 권한 추출 (재발급 시 필요)
            return e.getClaims().get("role", String.class);
        }
    }

    /**
     * JWT 토큰 유효성 검증
     * 서명 검증, 만료 시간 확인 등을 수행
     * 
     * @param token 검증할 JWT 토큰 문자열
     * @return 토큰이 유효하면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)           // 서명 키로 검증
                    .build()
                    .parseSignedClaims(token); // 토큰 파싱 및 검증
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰 만료
            System.out.println("토큰 검증 실패: 만료됨 - " + e.getMessage());
        } catch (JwtException e) {
            // 서명 불일치, 형식 오류 등 기타 JWT 관련 예외
            System.out.println("토큰 검증 실패: 기타 JWT 예외 - " + e.getClass().getSimpleName() + " - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // 잘못된 인자 (null, 빈 문자열 등)
            System.out.println("토큰 검증 실패: 잘못된 인자 - " + e.getMessage());
        }
        return false;
    }
}