package my_board.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequestDto {
    private String carrier;
    private String invoice;
}
