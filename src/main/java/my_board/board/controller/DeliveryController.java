package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.DeliveryRequestDto;
import my_board.board.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 택배 배송 조회 REST API 컨트롤러
 * 스마트택배 API를 통한 택배 배송 정보 조회 엔드포인트 제공
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 택배 배송 조회 API
     * 스마트택배 API를 호출하여 실시간 택배 배송 정보를 조회
     * 택배사 코드와 송장번호를 입력받아 배송 현황을 반환
     * POST /api/getTrackingDelivery
     * @param dto 택배 조회 정보 (carrier: 택배사 코드, invoice: 송장번호)
     * @return 200 OK - 스마트택배 API 응답 JSON
     */
    @PostMapping("/getTrackingDelivery")
    public ResponseEntity<?> getTracking(@RequestBody DeliveryRequestDto dto) {
        return ResponseEntity.ok(
                deliveryService.trackDelivery(dto.getCarrier(), dto.getInvoice())
        );
    }
}
