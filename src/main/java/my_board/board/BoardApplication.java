package my_board.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 메인 클래스
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
