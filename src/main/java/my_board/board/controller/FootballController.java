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

/**
 * 축구 경기 정보 조회 REST API 컨트롤러
 * RapidAPI의 API-Football을 통해 프리미어리그 경기 정보를 조회
 * 
 * @RestController: @Controller + @ResponseBody
 *   모든 메서드의 반환값을 HTTP 응답 본문에 직접 작성 (JSON 변환)
 * @RequestMapping: 기본 경로를 "/api"로 설정
 * @CrossOrigin: CORS 설정 (별도로 설정되어 있지만 명시적으로 추가)
 * 
 * TODO: API_KEY를 환경변수나 application.yml로 이동 권장
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FootballController {

    /**
     * RapidAPI 키
     * TODO: 실제 운영 환경에서는 @Value("${rapidapi.key}")로 주입받아야 함
     * 현재는 빈 문자열로 설정되어 있어 API 호출 시 401 에러 발생
     */
    private final String API_KEY = ""; // RapidAPI 키를 여기에 입력하거나 환경변수로 관리
    
    /**
     * API-Football 기본 URL
     */
    private final String BASE_URL = "https://api-football-v1.p.rapidapi.com/v3/fixtures";

    /**
     * 프리미어리그 경기 일정 조회 API
     * 
     * RapidAPI의 API-Football을 호출하여 프리미어리그(league=39) 
     * 2023 시즌 경기 일정을 조회
     * 
     * 엔드포인트: GET /api/fixtures
     * 
     * @return 200 OK - 경기 일정 정보 JSON
     *         500 Internal Server Error - API 호출 실패
     * 
     * 참고:
     * - league=39: 프리미어리그
     * - season=2023: 2023-2024 시즌
     * - Java 11의 HttpClient 사용 (별도 라이브러리 불필요)
     */
    @GetMapping("/fixtures")
    public ResponseEntity<String> getFixtures() {
        try {
            // Java 11+ HttpClient 생성
            HttpClient client = HttpClient.newHttpClient();
            
            // HTTP GET 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "?league=39&season=2023"))
                    .header("X-RapidAPI-Key", API_KEY)           // RapidAPI 인증 키
                    .header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
                    .GET()
                    .build();

            // 동기 방식으로 API 호출 및 응답 수신
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            // 응답 본문 반환
            return ResponseEntity.ok(response.body());

        } catch (Exception e) {
            // 예외 발생 시 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("API 요청 실패: " + e.getMessage());
        }
    }
}
