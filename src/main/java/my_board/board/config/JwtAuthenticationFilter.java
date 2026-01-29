package my_board.board.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import my_board.board.jwt.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import java.io.IOException;


/**
 * JWT 인증 필터
 * HTTP 요청의 Authorization 헤더에서 JWT 토큰을 추출하고 검증하여
 * Spring Security의 SecurityContext에 인증 정보를 설정
 * 
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    /**
     * JWT 토큰 생성 및 검증을 담당하는 프로바이더
     */
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * URL 패턴 매칭을 위한 Ant 스타일 경로 매처
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 요청을 필터링하여 JWT 토큰 기반 인증을 처리
     * 
     * 처리 순서:
     * 1. 공개 경로(로그인, 회원가입 등)인지 확인
     * 2. Authorization 헤더에서 JWT 토큰 추출
     * 3. 토큰 검증 및 유효한 경우 SecurityContext에 인증 정보 설정
     * 4. 다음 필터로 요청 전달
     * 
     * @param request  클라이언트 요청
     * @param response 서버 응답
     * @param chain    필터 체인
     * @throws IOException 입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // 1) 토큰 검사가 필요 없는 공개 경로인지 확인
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2) Authorization 헤더에서 JWT 토큰 추출
        String token = resolveToken(httpRequest);

        // 3) 토큰이 존재하고 유효한 경우 인증 객체 생성 및 SecurityContext에 설정
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 사용자 이메일 추출
            String email = jwtTokenProvider.getEmail(token);
            // 인증 객체 생성 (principal: 이메일, credentials: null, authorities: null)
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, null);
            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // 토큰이 없거나 검증 실패 시 인증 정보 제거
            // 401 Unauthorized는 Spring Security가 자동으로 처리
            SecurityContextHolder.clearContext();
        }
        
        // 4) 다음 필터로 요청 전달
        chain.doFilter(request, response);
    }

    /**
     * 공개 경로(인증이 필요 없는 경로)인지 확인
     * 
     * @param path 요청 URI 경로
     * @return 공개 경로면 true, 아니면 false
     */
    private boolean isPublicPath(String path) {
        return pathMatcher.match("/api/users/login", path)
                || pathMatcher.match("/api/users/register", path)
                || pathMatcher.match("/api/users/reissue", path)
                || pathMatcher.match("/api/users/check-nickname", path);
    }

    /**
     * HTTP 요청 헤더에서 JWT 토큰을 추출
     * Authorization 헤더의 "Bearer " 접두사를 제거하고 토큰만 반환
     * 
     * @param req HTTP 요청 객체
     * @return JWT 토큰 문자열, 없으면 null
     */
    private String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7); // "Bearer " 이후의 토큰 부분만 반환
        }
        return null;
    }
}
