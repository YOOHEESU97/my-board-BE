package my_board.board.repository;

import my_board.board.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * RefreshToken 엔티티에 대한 데이터 접근 계층 (Repository)
 * JpaRepository를 상속하여 기본 CRUD 기능 제공
 * 
 * Primary Key가 String(email) 타입임에 유의
 * 
 * 기본 제공 메서드:
 * - save(RefreshToken): Refresh Token 저장 (이메일이 같으면 덮어쓰기)
 * - findById(String email): 이메일로 Refresh Token 조회
 * - existsById(String email): 이메일로 Refresh Token 존재 여부 확인
 * - deleteById(String email): 이메일로 Refresh Token 삭제
 * 등...
 * 
 * 로그인 시 토큰 저장, 토큰 재발급 시 검증에 사용
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // 기본 메서드만 사용 (PK가 email이므로 별도 쿼리 메서드 불필요)
}
