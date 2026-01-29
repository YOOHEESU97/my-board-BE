package my_board.board.repository;

import my_board.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Post 엔티티에 대한 데이터 접근 계층 (Repository)
 * JpaRepository를 상속하여 기본 CRUD 기능 제공
 * 
 * 기본 제공 메서드:
 * - save(Post): 게시글 저장 또는 수정
 * - findById(Long): ID로 게시글 조회
 * - findAll(): 모든 게시글 조회
 * - findAll(Sort): 정렬된 게시글 목록 조회
 * - delete(Post): 게시글 삭제
 * - deleteById(Long): ID로 게시글 삭제
 * - existsById(Long): ID로 게시글 존재 여부 확인
 * - count(): 게시글 수 조회
 * 등...
 * 
 * 현재는 기본 메서드만 사용하며, 필요 시 커스텀 쿼리 메서드 추가 가능
 * 예: List<Post> findByNickname(String nickname);
 *     List<Post> findByTitleContaining(String keyword);
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본 메서드만 사용 (추가 쿼리 메서드 필요 시 여기에 정의)
}
