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


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto dto) {
        userService.register(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isNickname = userRepository.existsByNickname(nickname);
        if (isNickname) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        User user = userService.authenticate(dto.getEmail(), dto.getPassword());
        String accessToken = jwtTokenProvider.createToken(user.getEmail(),  user.getRole() , user.getNickname());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());

        // RefreshToken 저장
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email(user.getEmail())
                        .token(refreshToken)
                        .build()
        );

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "nickname", user.getNickname()
        ));
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody TokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();
        String accessToken = dto.getAccessToken();
        
        // 1. RefreshToken 유효성 검사
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. 이메일 추출
        String email = jwtTokenProvider.getEmail(refreshToken);

        // 3. DB에 저장된 refresh token과 일치하는지 확인
        RefreshToken saved = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("저장된 리프레시 토큰이 없습니다."));

        if(!saved.getToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("리프레시 토큰이 일치하지 않습니다.");
        }

        // 4. 새 access token 발급
        String newAccessToken = jwtTokenProvider.createToken(email, jwtTokenProvider.getRole(accessToken), null);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}