// ✅ UserRegisterDto.java (DTO)
package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    private String email;
    private String password;
    private String nickname;
}
