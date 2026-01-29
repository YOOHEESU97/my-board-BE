package my_board.board.service;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.UserRegisterDto;
import my_board.board.entity.User;
import my_board.board.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스
 * - 회원가입
 * - 로그인 인증
 * 
 * @Service: Spring의 서비스 계층 컴포넌트로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (DI)
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * 사용자 데이터 접근을 위한 Repository
     */
    private final UserRepository userRepository;
    
    /**
     * 비밀번호 암호화/검증을 위한 Encoder
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 회원가입 처리
     * 
     * 처리 과정:
     * 1. 이메일 중복 체크
     * 2. 비밀번호 암호화
     * 3. 기본 권한(ROLE_USER) 설정
     * 4. 데이터베이스에 저장
     * 
     * @param dto 회원가입 정보 (이메일, 비밀번호, 닉네임)
     * @throws RuntimeException 이미 가입된 이메일인 경우
     */
    public void register(UserRegisterDto dto) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        // 사용자 엔티티 생성
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))  // BCrypt로 비밀번호 암호화
                .nickname(dto.getNickname())
                .role("ROLE_USER")  // 기본 권한 설정
                .build();

        // 데이터베이스에 저장
        userRepository.save(user);
    }

    /**
     * 사용자 로그인 인증 처리
     * 이메일과 비밀번호를 검증하여 사용자 정보 반환
     * 
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호 (평문)
     * @return 인증된 User 객체
     * @throws UsernameNotFoundException 이메일이 존재하지 않는 경우
     * @throws IllegalArgumentException  비밀번호가 일치하지 않는 경우
     */
    public User authenticate(String email, String password) {
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));
        
        // 비밀번호 검증 (입력된 평문과 저장된 암호화된 비밀번호 비교)
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        return user;
    }
}
