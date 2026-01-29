package my_board.board.repository;

import my_board.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Post 엔티티에 대한 데이터 접근 계층 (Repository)
 * JpaRepository를 상속하여 기본 CRUD 기능 제공
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 메서드만 사용 (추가 쿼리 메서드 필요 시 여기에 정의)
}
