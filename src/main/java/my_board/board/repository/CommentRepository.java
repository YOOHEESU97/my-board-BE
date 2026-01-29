package my_board.board.repository;

import my_board.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    /**
     * 특정 게시글의 모든 댓글을 작성 시간 오름차순으로 조회
     * 댓글과 대댓글을 모두 가져와서 프론트엔드에서 계층 구조로 구성
     * 쿼리 메서드 네이밍 규칙:
     * - findBy: 조회 쿼리
     * - PostId: Post의 id 필드로 조회
     * - OrderBy: 정렬
     * - CreatedAtAsc: createdAt 필드로 오름차순 정렬
     * @param postId 조회할 게시글 ID
     * @return 해당 게시글의 댓글 리스트 (작성 시간 오름차순)
     */
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}
