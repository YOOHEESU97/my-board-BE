package my_board.board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private Long postId;
    private Long parentId;
    private String content;
    private String writerNickname;
    private String writerEmail;
    private LocalDateTime createdAt;

}
