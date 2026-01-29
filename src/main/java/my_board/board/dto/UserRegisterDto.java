package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청 DTO (Data Transfer Object)
 * 클라이언트로부터 회원가입 정보를 받기 위한 데이터 전송 객체
 * 
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 */
@Getter
@Setter
public class UserRegisterDto {
    /**
     * 사용자 이메일 (로그인 ID, 중복 불가)
     */
    private String email;
    
    /**
     * 사용자 비밀번호 (평문)
     * 서버에서 BCrypt를 사용하여 암호화 후 저장
     */
    private String password;
    
    /**
     * 사용자 닉네임 (화면 표시용, 중복 불가)
     */
    private String nickname;
}
