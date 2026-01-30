package my_board.board.service;

import lombok.RequiredArgsConstructor;
import my_board.board.dto.CommentRequestDto;
import my_board.board.dto.CommentResponseDto;
import my_board.board.entity.Comment;
import my_board.board.entity.Post;
import my_board.board.entity.User;
import my_board.board.repository.CommentRepository;
import my_board.board.repository.PostRepository;
import my_board.board.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 댓글 관련 비즈니스 로직을 처리하는 서비스
 * - 댓글 작성 (일반 댓글 및 대댓글)
 * - 댓글 목록 조회
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    /**
     * 댓글 데이터 접근을 위한 Repository
     */
    private final CommentRepository commentRepository;
    
    /**
     * 게시글 데이터 접근을 위한 Repository
     */
    private final PostRepository postRepository;
    
    /**
     * 사용자 데이터 접근을 위한 Repository
     */
    private final UserRepository userRepository;

    /**
     * 댓글 작성 (일반 댓글 및 대댓글)
     * 
     * 처리 과정:
     * 1. 게시글 존재 여부 확인
     * 2. 사용자 존재 여부 확인
     * 3. 대댓글인 경우 부모 댓글 확인
     * 4. 댓글 생성 및 저장
     * 5. DTO로 변환하여 반환
     * 
     * @param postId    댓글을 작성할 게시글 ID
     * @param userEmail 댓글 작성자 이메일
     * @param dto       댓글 내용 및 부모 댓글 ID (대댓글인 경우)
     * @return 생성된 댓글 정보
     */
    public CommentResponseDto addComment(Long postId, String userEmail, CommentRequestDto dto) {
        // 게시글 조회 및 검증
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 사용자 조회 및 검증
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 대댓글인 경우 부모 댓글 조회
        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
        }

        // 댓글 생성 및 저장
        Comment comment = Comment.create(post, user, parent, dto.getContent());
        Comment saved = commentRepository.save(comment);

        // 응답 DTO로 변환하여 반환
        return CommentResponseDto.builder()
                .id(saved.getId())
                .postId(saved.getPost().getId())
                .parentId(saved.getParent() != null ? saved.getParent().getId() : null)
                .content(saved.getContent())
                .writerNickname(saved.getUser().getNickname())
                .writerEmail(saved.getUser().getEmail())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    /**
     * 특정 게시글의 모든 댓글 조회
     * Transactional
     * - 읽기 전용 트랜잭션으로 성능 최적화
     * @param postId 게시글 ID
     * @return 댓글 리스트
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId) {
        // 게시글의 모든 댓글 조회 (작성 시간 오름차순)
        List<Comment> list = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        // 엔티티를 DTO로 변환
        return list.stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getId())
                        .postId(c.getPost().getId())
                        .parentId(c.getParent() != null ? c.getParent().getId() : null)
                        .content(c.getContent())
                        .writerNickname(c.getUser().getNickname())
                        .writerEmail(c.getUser().getEmail())
                        .createdAt(c.getCreatedAt())
                        .deleted(c.getDeleted())
                        .build())
                .toList();
    }

    /**
     * 댓글 삭제
     * @param commentId 삭제할 댓글 ID
     * @param userEmail 삭제를 요청한 사용자 이메일
     */
    public void deleteComment(Long commentId, String userEmail) {
        // 댓글 조회 및 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 작성자 본인인지 확인 (보안 체크)
        if (!comment.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        // Soft Delete: 내용을 "삭제 처리 된 댓글입니다."로 변경
        comment.markAsDeleted();
    }
}
