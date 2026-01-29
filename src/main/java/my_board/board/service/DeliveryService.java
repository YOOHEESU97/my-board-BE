package my_board.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 택배 배송 조회 서비스
 * 스마트택배 API를 사용하여 택배 배송 정보를 조회
 * 
 * API 문서: https://tracking.sweettracker.co.kr:8443/templates/app.html#/
 * 
 * @Service: Spring의 서비스 계층 컴포넌트로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (DI)
 */
@Service
@RequiredArgsConstructor
public class DeliveryService {

    /**
     * 스마트택배 API 키
     * Git에 올릴때 노출되지않게 application.properties에서 주입
     */
    @Value("${smarttracker.key}")
    private String SMART_API_KEY;
    
    /**
     * HTTP 통신을 위한 RestTemplate
     */
    private final RestTemplate restTemplate;

    /**
     * 택배 배송 조회
     * 
     * 스마트택배 API를 호출하여 택배 배송 정보를 조회
     * - 택배사 코드와 송장번호로 배송 현황 조회
     * - 실시간 배송 위치 및 배송 단계 정보 제공
     * 
     * @param carrierCode   택배사 코드 (예: "04" - CJ대한통운, "05" - 한진택배)
     * @param invoiceNumber 송장번호
     * @return Sweet Tracker API 응답 JSON 문자열
     */
    
    public Object trackDelivery(String carrierCode, String invoiceNumber) {
        // API URL 생성 (쿼리 파라미터 포함)
        String url = String.format(
                "https://info.sweettracker.co.kr/api/v1/trackingInfo?t_key=%s&t_code=%s&t_invoice=%s",
                SMART_API_KEY, 
                carrierCode.trim(),      // 앞뒤 공백 제거
                invoiceNumber.trim()     // 앞뒤 공백 제거
        );

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=UTF-8");

        // HTTP 요청 엔티티 생성 (헤더만 포함, 본문 없음)
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // POST 요청 전송 및 응답 수신
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        
        // 응답 본문 반환 (JSON 문자열)
        return response.getBody();
    }

}
