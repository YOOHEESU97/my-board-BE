package my_board.board.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeliveryService {

    private final String SMART_API_KEY = "key"; // API 키 넣기

    public Object trackDelivery(String carrierCode, String invoiceNumber) {
        String url = "https://info.sweettracker.co.kr/api/v1/trackingInfo";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("t_key", SMART_API_KEY);
        requestBody.put("t_code", carrierCode);
        requestBody.put("t_invoice", invoiceNumber);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(url, request, Object.class);
        return response.getBody();
    }
}
