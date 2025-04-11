package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.LoginDto;
import my_board.board.dto.UserRegisterDto;
import my_board.board.entity.User;
import my_board.board.jwt.JwtTokenProvider;
import my_board.board.repository.UserRepository;
import my_board.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    
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
        String token = jwtTokenProvider.createToken(user.getEmail(),  user.getRole() , user.getNickname());
        return ResponseEntity.ok(token);
    }

    /*@PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        
        if(jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmail(refreshToken);
            String role = jwtTokenProvider.getRole(refreshToken);
            String newAccessToken = jwtTokenProvider.createAccessToken(email,role);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("RefreshToken 만료");*/
    }
}
