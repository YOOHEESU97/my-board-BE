package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.DeliveryRequestDto;
import my_board.board.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping("/getTrackingDelivery")
    public ResponseEntity<?> getTracking(@RequestBody DeliveryRequestDto dto) {
        return ResponseEntity.ok(
                deliveryService.trackDelivery(dto.getCarrier(), dto.getInvoice())
        );
    }
}
