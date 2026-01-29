package my_board.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스
 * HTTP 통신을 위한 RestTemplate 빈을 등록
 * 
 * RestTemplate:
 * - Spring에서 제공하는 HTTP 클라이언트
 * - 외부 API 호출에 사용
 * - 동기 방식 통신 지원
 * 
 * 참고: Spring 5.0부터 WebClient가 권장되지만, 
 * 간단한 용도에는 RestTemplate도 충분히 사용 가능
 * 
 * @Configuration: Spring 설정 클래스로 등록
 */
@Configuration
public class RestTemplateConfig {

    /**
     * RestTemplate 빈 등록
     * 
     * Bean으로 등록하여 애플리케이션 전역에서 재사용
     * - 매번 새로운 인스턴스를 생성하는 것보다 효율적
     * - Connection Pool 관리 등 최적화 가능
     * 
     * @return RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
