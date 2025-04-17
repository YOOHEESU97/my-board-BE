package my_board.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class DeliveryService {

    @Value("${smarttracker.key}")
    private String SMART_API_KEY; // API 키 넣기

    public Object trackDelivery(String carrierCode, String invoiceNumber) {
        String url = String.format(
                "https://info.sweettracker.co.kr/api/v1/trackingInfo?t_key=%s&t_code=%s&t_invoice=%s",
                SMART_API_KEY, carrierCode.trim(), invoiceNumber.trim()
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=UTF-8");

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

}
