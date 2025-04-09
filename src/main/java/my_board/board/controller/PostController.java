package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.PostDto;
import my_board.board.entity.Post;
import my_board.board.repository.PostRepository;
import my_board.board.service.PostService;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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
}
