package my_board.board.repository;

import my_board.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User 엔티티에 대한 데이터 접근 계층 (Repository)
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 이메일로 사용자 조회
     * 로그인 시 사용자 인증에 사용
     * 
     * @param email 조회할 사용자 이메일
     * @return 사용자 Optional 객체 (존재하지 않으면 Optional.empty())
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 닉네임 중복 확인
     * 회원가입 시 닉네임 중복 체크에 사용
     * 
     * @param nickname 확인할 닉네임
     * @return 닉네임이 존재하면 true, 아니면 false
     */
    boolean existsByNickname(String nickname);
}
