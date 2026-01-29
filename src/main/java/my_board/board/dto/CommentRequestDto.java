package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글 작성 요청 DTO (Data Transfer Object)
 * 클라이언트로부터 댓글 작성 정보를 받기 위한 데이터 전송 객체
 * 일반 댓글과 대댓글 모두 동일한 DTO 사용
 * 
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 */
@Getter
@Setter
public class CommentRequestDto {
    /**
     * 댓글 내용
     * 최대 500자까지 저장 가능 (엔티티에서 length = 500으로 제한)
     */
    private String content;
    
    /**
     * 부모 댓글 ID
     * - null이면 최상위 댓글 (일반 댓글)
     * - 값이 있으면 대댓글 (답글)
     */
    private Long parentId;
}
