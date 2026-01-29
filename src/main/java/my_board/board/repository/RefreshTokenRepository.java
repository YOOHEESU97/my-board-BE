package my_board.board.repository;

import my_board.board.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RefreshToken 엔티티에 대한 데이터 접근 계층 (Repository)
 * 로그인 시 토큰 저장, 토큰 재발급 시 검증에 사용
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // 기본 메서드만 사용 (PK가 email이므로 별도 쿼리 메서드 불필요)
}
