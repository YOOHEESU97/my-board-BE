package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 수정 요청 DTO (Data Transfer Object)
 * 클라이언트로부터 게시글 수정 정보를 받기 위한 데이터 전송 객체
 * 
 * 작성자 정보(email, nickname)는 수정 불가하므로 포함하지 않음
 * 
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 */
@Getter
@Setter
public class PostUpdateDto {
    /**
     * 수정할 게시글 제목
     */
    private String title;
    
    /**
     * 수정할 게시글 내용
     */
    private String content;
}
