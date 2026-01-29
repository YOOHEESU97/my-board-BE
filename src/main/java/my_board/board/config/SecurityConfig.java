package my_board.board.config;

import lombok.RequiredArgsConstructor;
import my_board.board.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 설정 클래스
 * - JWT 기반 인증/인가 설정
 * - CORS 설정
 * - 비밀번호 암호화 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * JWT 토큰 생성 및 검증을 담당하는 Provider
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * CORS(Cross-Origin Resource Sharing) 설정
     * 프론트엔드(로컬포트 > 5173)에서 백엔드 API 호출을 허용하기 위한 설정
     * 
     * @return CORS 설정이 적용된 CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 허용할 출처(Origin) 설정 - 프론트엔드 개발 서버
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        // 허용할 HTTP 메서드 설정
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 허용할 헤더 설정 - 모든 헤더 허용
        config.setAllowedHeaders(List.of("*"));
        // 자격 증명(쿠키, Authorization 헤더 등) 포함 허용
        config.setAllowCredentials(true);

        // 모든 경로에 대해 CORS 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    /**
     * Spring Security 필터 체인 설정
     * - JWT 기반 인증/인가 처리
     * @param http HttpSecurity 설정 객체
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // HTTP Basic 인증 비활성화 (JWT 사용)
                .httpBasic(AbstractHttpConfigurer::disable)
                // CSRF 보호 비활성화 (REST API에서는 불필요)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS 활성화 (위에서 정의한 설정 사용)
                .cors(Customizer.withDefaults())
                // 요청에 대한 인가 규칙 설정
                .authorizeHttpRequests(auth -> auth
                        // 인증 없이 접근 가능한 엔드포인트 (로그인, 회원가입, 닉네임 중복 확인, 토큰 재발급)
                        .requestMatchers("/api/users/login", "/api/users/register", "/api/users/check-nickname", "/api/users/reissue").permitAll()
                        // 게시글 조회는 인증 없이 가능
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     * BCrypt 해시 알고리즘을 사용하여 안전하게 비밀번호를 저장
     * 
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
