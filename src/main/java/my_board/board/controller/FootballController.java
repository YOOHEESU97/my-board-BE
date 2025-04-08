package my_board.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FootballController {

    private final String API_KEY = ""; // RapidAPI 키
    private final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/fixtures";

    @GetMapping("/fixtures")
    public ResponseEntity<String> getFixtures() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "?league=39&season=2023"))
                    .header("X-RapidAPI-Key", API_KEY)
                    .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return ResponseEntity.ok(response.body());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API 요청 실패: " + e.getMessage());
        }
    }
}
