package my_board.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 게시판 애플리케이션의 메인 클래스
 * Spring Boot 애플리케이션의 진입점(Entry Point)
 * 
 * @SpringBootApplication: 다음 세 가지 어노테이션을 통합
 *   - @Configuration: Spring 설정 클래스임을 명시
 *   - @EnableAutoConfiguration: Spring Boot의 자동 설정 활성화
 *   - @ComponentScan: 컴포넌트 스캔을 통해 Bean 등록
 */
@SpringBootApplication
public class BoardApplication {

	/**
	 * 애플리케이션 실행 메서드
	 * @param args 커맨드 라인 인자
	 */
	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
