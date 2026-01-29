package my_board.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 댓글 응답 DTO (Data Transfer Object)
 * 서버에서 클라이언트로 댓글 정보를 전달하기 위한 데이터 전송 객체
 * 
 * @Getter: Lombok을 통한 getter 자동 생성
 * @Builder: 빌더 패턴 지원 (객체 생성 편의성)
 */
@Getter
@Builder
public class CommentResponseDto {
    /**
     * 댓글 고유 식별자
     */
    private Long id;
    
    /**
     * 댓글이 속한 게시글 ID
     */
    private Long postId;
    
    /**
     * 부모 댓글 ID
     * - null이면 최상위 댓글
     * - 값이 있으면 대댓글
     */
    private Long parentId;
    
    /**
     * 댓글 내용
     */
    private String content;
    
    /**
     * 작성자 닉네임
     */
    private String writerNickname;
    
    /**
     * 작성자 이메일
     * 본인이 작성한 댓글인지 확인하기 위해 포함
     */
    private String writerEmail;
    
    /**
     * 댓글 작성 시간
     * ISO 8601 형식으로 JSON 직렬화됨
     * 예: "2024-01-29T12:30:45"
     */
    private LocalDateTime createdAt;

}
