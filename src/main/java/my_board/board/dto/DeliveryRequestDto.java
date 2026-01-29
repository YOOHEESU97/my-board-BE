package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 택배 배송 조회 요청 DTO (Data Transfer Object)
 * 클라이언트로부터 택배 조회 정보를 받기 위한 데이터 전송 객체
 * 
 * @Getter/@Setter: Lombok을 통한 getter/setter 자동 생성
 */
@Getter
@Setter
public class DeliveryRequestDto {
    /**
     * 택배사 코드
     * 
     * 주요 택배사 코드:
     * - "04": CJ대한통운
     * - "05": 한진택배
     * - "08": 롯데택배
     * - "01": 우체국택배
     * - "06": 로젠택배
     * - "11": 일양로지스
     * - "46": CU 편의점택배
     * - "24": GS Postbox 택배
     * 등...
     */
    private String carrier;
    
    /**
     * 송장번호 (운송장번호)
     * 하이픈(-) 없이 숫자만 입력
     * 예: "1234567890123"
     */
    private String invoice;
}
