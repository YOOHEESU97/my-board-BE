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
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Transactional
public class PostController {

    private final PostService postService;
    
    /**
     * 게시글(조회 및 삭제 시)
     */
    private final PostRepository postRepository;

    /**
     * 게시글 작성 API
     * JWT 인증이 필요한 엔드포인트 (SecurityConfig에서 설정)
     * POST /api/create-posts
     * dto 게시글 정보 (제목, 내용, 작성자 이메일, 닉네임)
     */
    @PostMapping("/create-posts")
    public ResponseEntity<?> createPost(@RequestBody PostDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok("게시글 등록 완료");
    }

    /**
     * 전체 게시글 목록 조회 API
     * 인증 없이 접근 가능 (공개 API)
     * GET /api/getPosts
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
     * GET /api/posts/{id}
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
     * PUT /api/posts/{id}
     * id  수정할 게시글 ID
     * dto 수정할 내용 (제목, 내용)
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
     * DELETE /api/posts/{id}
     * id 삭제할 게시글 ID
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: 게시글이 아직 존재합니다.");
        }

        return ResponseEntity.ok("삭제 완료");
    }
}
