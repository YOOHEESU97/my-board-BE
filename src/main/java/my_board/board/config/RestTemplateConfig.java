package my_board.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스
 * HTTP 통신을 위한 RestTemplate 빈을 등록
 * RestTemplate:
 * - Spring에서 제공하는 HTTP 클라이언트
 * - 외부 API 호출에 사용
 * - 동기 방식 통신 지원
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 빈 등록
     * Bean으로 등록하여 애플리케이션 전역에서 재사용
     * @return RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
