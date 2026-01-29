package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.CommentRequestDto;
import my_board.board.dto.CommentResponseDto;
import my_board.board.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable("postId") Long postId) {
        List<CommentResponseDto> comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDto dto,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal(); // JWT 필터에서 세팅한 email
        CommentResponseDto result = commentService.addComment(postId, email, dto);
        return ResponseEntity.ok(result);
    }
}
