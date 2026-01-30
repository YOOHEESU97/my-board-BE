package my_board.board.controller;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.CommentRequestDto;
import my_board.board.dto.CommentResponseDto;
import my_board.board.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 댓글 관련 REST API 컨트롤러
 * - 댓글 조회, 작성
 * - RESTful URL 설계: /api/posts/{postId}/comments
 */
@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 특정 게시글의 댓글 목록 조회 API
     * 인증 없이 접근 가능
     * GET /api/posts/{postId}/comments
     * postId 조회할 게시글 ID
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable("postId") Long postId) {
        List<CommentResponseDto> comments = commentService.getComments(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 댓글 작성 API (일반 댓글 및 대댓글)
     * JWT 인증이 필요한 엔드포인트
     * POST /api/posts/{postId}/comments
     * postId         댓글을 작성할 게시글 ID (URL 경로에서 추출)
     * dto            댓글 내용 및 부모 댓글 ID (대댓글인 경우)
     * authentication Spring Security의 인증 객체, JwtAuthenticationFilter에서 설정한 사용자 이메일 포함
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable("postId") Long postId,
            @RequestBody CommentRequestDto dto,
            Authentication authentication
    ) {
        // JWT 필터에서 SecurityContext에 설정한 사용자 이메일 추출
        // JwtAuthenticationFilter에서 principal로 이메일을 설정
        String email = (String) authentication.getPrincipal();
        
        // 댓글 생성 및 저장
        CommentResponseDto result = commentService.addComment(postId, email, dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 댓글 삭제 API (Soft Delete)
     * JWT 인증이 필요한 엔드포인트
     * DELETE /api/posts/{postId}/comments/{commentId}
     * postId   게시글 ID
     * commentId    삭제할 댓글 ID
     * authentication Spring Security의 인증 객체
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            Authentication authentication
    ) {
        // JWT 필터에서 SecurityContext에 설정한 사용자 이메일 추출
        String email = (String) authentication.getPrincipal();
        
        // 댓글 삭제 (Soft Delete)
        commentService.deleteComment(commentId, email);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}
