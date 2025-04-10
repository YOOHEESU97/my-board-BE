// ✅ UserController.java
package my_board.board.controller;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import my_board.board.dto.LoginRequestDto;
import my_board.board.dto.UserRegisterDto;
import my_board.board.entity.User;
import my_board.board.jwt.JwtTokenProvider;
import my_board.board.repository.UserRepository;
import my_board.board.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

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
        System.out.print("닉네임 >> " + nickname);
        boolean isNickname = userRepository.existsByNickname(nickname);
        if (isNickname) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("중복된 닉네임입니다.");
        }
        return ResponseEntity.ok("사용 가능한 닉네임입니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        User user = userService.authenticate(dto.getEmail(), dto.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패");
        }

        //String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole());
        //return ResponseEntity.ok().body(Collection.singletonMap("token", token));
        return ResponseEntity.ok("나중에 수정");
    }
}
