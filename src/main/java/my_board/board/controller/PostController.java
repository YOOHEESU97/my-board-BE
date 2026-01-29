package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.PostDto;
import my_board.board.dto.PostUpdateDto;
import my_board.board.entity.Post;
import my_board.board.repository.PostRepository;
import my_board.board.service.PostService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 게시글 관련 REST API 컨트롤러
 * - 게시글 작성, 조회, 수정, 삭제 등의 CRUD 엔드포인트 제공
 * 
 * @RestController: @Controller + @ResponseBody
 *   모든 메서드의 반환값을 HTTP 응답 본문에 직접 작성 (JSON 변환)
 * @RequestMapping: 기본 경로를 "/api"로 설정
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (DI)
 * @Transactional: 클래스 레벨에 선언하여 모든 public 메서드에 트랜잭션 적용
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Transactional
public class PostController {

    /**
     * 게시글 비즈니스 로직 처리
     */
    private final PostService postService;
    
    /**
     * 게시글 데이터 접근 (조회 및 삭제 시 직접 사용)
     */
    private final PostRepository postRepository;

    /**
     * 게시글 작성 API
     * JWT 인증이 필요한 엔드포인트 (SecurityConfig에서 설정)
     * 
     * 엔드포인트: POST /api/create-posts
     * 
     * @param dto 게시글 정보 (제목, 내용, 작성자 이메일, 닉네임)
     * @return 200 OK - 게시글 작성 성공 메시지
     */
    @PostMapping("/create-posts")
    public ResponseEntity<?> createPost(@RequestBody PostDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok("게시글 등록 완료");
    }

    /**
     * 전체 게시글 목록 조회 API
     * 인증 없이 접근 가능 (공개 API)
     * 
     * 엔드포인트: GET /api/getPosts
     * 
     * @return 200 OK - 게시글 리스트 (작성 시간 내림차순 정렬)
     */
    @GetMapping("/getPosts")
    public ResponseEntity<List<Post>> getAllPosts() {
        // 작성 시간 내림차순으로 정렬 (최신 글이 먼저 표시)
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
        return ResponseEntity.ok(posts);
    }

    /**
     * 특정 게시글 상세 조회 API
     * 인증 없이 접근 가능 (공개 API)
     * 
     * 엔드포인트: GET /api/posts/{id}
     * 
     * @param id 조회할 게시글 ID
     * @return 200 OK - 게시글 상세 정보
     *         404 Not Found - 게시글을 찾을 수 없음
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시판을 찾을 수 없습니다.");
        }
    }

    /**
     * 게시글 수정 API
     * JWT 인증이 필요한 엔드포인트
     * 
     * 엔드포인트: PUT /api/posts/{id}
     * 
     * @param id  수정할 게시글 ID
     * @param dto 수정할 내용 (제목, 내용)
     * @return 200 OK - 수정 완료 메시지
     *         404 Not Found - 게시글을 찾을 수 없음
     */
    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable("id") Long id,
            @RequestBody PostUpdateDto dto
    ) {
        postService.update(id, dto);
        return ResponseEntity.ok("수정 완료");
    }

    /**
     * 게시글 삭제 API
     * JWT 인증이 필요한 엔드포인트
     * 
     * 삭제 처리 과정:
     * 1. 게시글 존재 여부 확인
     * 2. 게시글 삭제
     * 3. 삭제 성공 여부 확인 (선택적, 안전성 검증)
     * 
     * 엔드포인트: DELETE /api/posts/{id}
     * 
     * @param id 삭제할 게시글 ID
     * @return 200 OK - 삭제 완료 메시지
     *         404 Not Found - 게시글을 찾을 수 없음
     *         500 Internal Server Error - 삭제 실패 (드문 경우)
     */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {

        // 1. 게시글 존재 여부 확인
        // 선택적 단계: 삭제 전에 미리 확인하여 명확한 에러 메시지 제공
        if (!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 게시글입니다.");
        }

        // 2. 게시글 삭제
        postRepository.deleteById(id);

        // 3. 삭제 성공 여부 확인 (선택적, 안전성 검증)
        boolean stillExists = postRepository.existsById(id);

        if (stillExists) {
            // 정상적으로는 발생하지 않지만, 트랜잭션 문제나 제약 조건 위반 시 발생 가능
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: 게시글이 아직 존재합니다.");
        }

        return ResponseEntity.ok("삭제 완료");
    }
}
