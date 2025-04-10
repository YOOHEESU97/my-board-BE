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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Transactional
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @PostMapping("/create-posts")
    public ResponseEntity<?> createPost(@RequestBody PostDto dto) {
        postService.createPost(dto);
        return ResponseEntity.ok("게시글 등록 완료");
    }

    @GetMapping("/getPosts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시판을 찾을 수 없습니다.");
        }
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable("id") Long id,
            @RequestBody PostUpdateDto dto
    ) {
        postService.update(id, dto);
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable("id") Long id) {

        // 존재 여부 확인 (없어도됨)
        if (!postRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 게시글입니다.");
        }

        postRepository.deleteById(id);

        // 삭제 확인
        boolean stillExists = postRepository.existsById(id);

        if (stillExists) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("삭제 실패: 게시글이 아직 존재합니다.");
        }


        return ResponseEntity.ok("삭제 완료");
    }
}
