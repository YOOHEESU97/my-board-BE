package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDto {
    private String title;
    private String content;
    private String email;
    private String nickname;
}
