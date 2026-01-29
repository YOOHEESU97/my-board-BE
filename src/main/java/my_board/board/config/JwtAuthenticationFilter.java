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


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        // 1) 토큰 검사 안 할 경로 (로그인/회원가입/토큰재발급/닉네임중복체크 등)
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2) 헤더에서 토큰 추출
        String token = resolveToken(httpRequest);

        // 3) 토큰이 있고 && 유효하면 → 인증 객체 세팅

        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmail(token);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // 토큰 없거나, 검증 실패 → 인증정보 제거 (401은 Security가 판단)
            SecurityContextHolder.clearContext();
        }
        // 4) 다음 필터로 진행
        chain.doFilter(request,response);
    }

    private boolean isPublicPath(String path) {
        return pathMatcher.match("/api/users/login", path)
                || pathMatcher.match("/api/users/register", path)
                || pathMatcher.match("/api/users/reissue", path)
                || pathMatcher.match("/api/users/check-nickname", path);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
