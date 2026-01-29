package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 게시글 작성 요청 DTO (Data Transfer Object)
 * 클라이언트로부터 게시글 작성 정보를 받기 위한 데이터 전송 객체
 * 
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 */
@Getter
@Setter
public class PostDto {
    /**
     * 게시글 제목
     */
    private String title;
    
    /**
     * 게시글 내용
     */
    private String content;
    
    /**
     * 작성자 이메일
     * JWT 토큰에서 추출하거나 클라이언트에서 전송
     */
    private String email;
    
    /**
     * 작성자 닉네임
     * 게시글 목록에서 바로 표시하기 위해 비정규화하여 저장
     */
    private String nickname;
}
