package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 토큰 재발급 요청 DTO (Data Transfer Object)
 * Access Token 재발급 시 필요한 토큰 정보를 받기 위한 데이터 전송 객체
 * 
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 */
@Getter
@Setter
public class TokenRequestDto {
    /**
     * 만료된 Access Token
     * 사용자 권한(role) 정보를 추출하기 위해 필요
     * 만료된 토큰에서도 claims 정보는 읽을 수 있음
     */
    private String accessToken;
    
    /**
     * 유효한 Refresh Token
     * 새로운 Access Token을 발급받기 위한 인증 토큰
     * 유효성 검증 후 데이터베이스의 토큰과 비교
     */
    private String refreshToken;
}
