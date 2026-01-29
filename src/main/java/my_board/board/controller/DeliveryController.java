package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.DeliveryRequestDto;
import my_board.board.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 택배 배송 조회 REST API 컨트롤러
 * Sweet Tracker API를 통한 택배 배송 정보 조회 엔드포인트 제공
 * 
 * @RestController: @Controller + @ResponseBody
 *   모든 메서드의 반환값을 HTTP 응답 본문에 직접 작성 (JSON 변환)
 * @RequestMapping: 기본 경로를 "/api"로 설정
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (DI)
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    /**
     * 택배 배송 조회 비즈니스 로직 처리
     */
    private final DeliveryService deliveryService;

    /**
     * 택배 배송 조회 API
     * 
     * Sweet Tracker API를 호출하여 실시간 택배 배송 정보를 조회
     * 택배사 코드와 송장번호를 입력받아 배송 현황을 반환
     * 
     * 엔드포인트: POST /api/getTrackingDelivery
     * 
     * @param dto 택배 조회 정보 (carrier: 택배사 코드, invoice: 송장번호)
     * @return 200 OK - Sweet Tracker API 응답 JSON
     *         (배송 단계, 현재 위치, 배송 이력 등 포함)
     * 
     * 택배사 코드 예시:
     * - "04": CJ대한통운
     * - "05": 한진택배
     * - "08": 롯데택배
     * - "01": 우체국택배
     * 등...
     */
    @PostMapping("/getTrackingDelivery")
    public ResponseEntity<?> getTracking(@RequestBody DeliveryRequestDto dto) {
        return ResponseEntity.ok(
                deliveryService.trackDelivery(dto.getCarrier(), dto.getInvoice())
        );
    }
}
