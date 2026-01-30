package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.LoginDto;
import my_board.board.dto.TokenRequestDto;
import my_board.board.dto.UserRegisterDto;
import my_board.board.entity.RefreshToken;
import my_board.board.entity.User;
import my_board.board.jwt.JwtTokenProvider;
import my_board.board.repository.RefreshTokenRepository;
import my_board.board.repository.UserRepository;
import my_board.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * 사용자 관련 REST API 컨트롤러
 * - 회원가입, 로그인, 닉네임 중복 확인, 토큰 재발급
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    
    /**
     * 사용자 데이터 접근 (닉네임 중복 체크, 토큰 재발급 시 사용)
     */
    private final UserRepository userRepository;
    
    /**
     * JWT 토큰 생성 및 검증
     */
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * Refresh Token 저장소
     */
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 회원가입 API
     * 
     * 엔드포인트: POST /api/users/register
     * 
     * @param dto 회원가입 정보 (이메일, 비밀번호, 닉네임)
     * @return 200 OK - 회원가입 성공 메시지
     *         400 Bad Request - 이미 가입된 이메일인 경우 (예외 발생)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto) {
        userService.register(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    /**
     * 닉네임 중복 확인 API
     * 회원가입 시 실시간으로 닉네임 중복을 체크
     * 
     * GET /api/users/check-nickname?nickname=홍길동
     * 
     * @param nickname 확인할 닉네임
     * @return 200 OK - 사용 가능한 닉네임
     *         409 Conflict - 중복된 닉네임
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isNickname = userRepository.existsByNickname(nickname);
        if (isNickname) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    /**
     * 로그인 API
     * 
     * 처리 과정:
     * 1. 이메일과 비밀번호로 사용자 인증
     * 2. Access Token 생성 (유효기간: 1시간)
     * 3. Refresh Token 생성 (유효기간: 7일)
     * 4. Refresh Token을 데이터베이스에 저장
     * 5. 토큰과 사용자 정보를 클라이언트에 반환
     * 
     * 엔드포인트: POST /api/users/login
     * 
     * @param dto 로그인 정보 (이메일, 비밀번호)
     * @return 200 OK - Access Token, Refresh Token, 닉네임
     *         401 Unauthorized - 인증 실패 (이메일 또는 비밀번호 오류)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        // 사용자 인증 (이메일 및 비밀번호 검증)
        User user = userService.authenticate(dto.getEmail(), dto.getPassword());
        
        // Access Token 생성 (사용자 이메일, 권한, 닉네임 포함)
        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRole(), user.getNickname());
        
        // Refresh Token 생성 (사용자 이메일만 포함)
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // Refresh Token을 데이터베이스에 저장
        // 동일한 이메일이 이미 존재하면 덮어쓰기 (PK가 email이므로)
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email(user.getEmail())
                        .token(refreshToken)
                        .build()
        );

        // 클라이언트에 토큰 및 사용자 정보 반환
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "nickname", user.getNickname()
        ));
    }

    /**
     * AccessToken 재발급 API
     * AccessToken 만료되었을 때 RefreshToken을 사용하여 새 AccessToken을 발급
     * 처리 과정:
     * 1. RefreshToken 유효성 검증
     * 2. RefreshToken에서 사용자 이메일 추출
     * 3. 데이터베이스에 저장된 Refresh oken과 비교 검증
     * 4. 기존 AccessToken에서 권한 정보 추출
     * 5. 새로운 AccessToken 생성 및 반환
     * POST /api/users/reissue

     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();
        String accessToken = dto.getAccessToken();

        // 1. Refresh Token 유효성 검사 (만료 여부, 서명 검증 등)
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. Refresh Token에서 사용자 정보 추출
        String email = jwtTokenProvider.getEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        String nickname = user.getNickname();

        // 3. 데이터베이스에 저장된 Refresh Token과 일치하는지 확인
        // (토큰 탈취 방지: DB에 저장된 토큰과 다르면 거부)
        RefreshToken saved = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("저장된 리프레시 토큰이 없습니다."));

        if(!saved.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 일치하지 않습니다.");
        }

        // 4. 새로운 Access Token 발급
        // 기존 Access Token에서 권한(role) 추출 (만료된 토큰에서도 추출 가능)
        String newAccessToken = jwtTokenProvider.createToken(email, jwtTokenProvider.getRole(accessToken), nickname);
        
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}