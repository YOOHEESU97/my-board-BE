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

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponseDto addComment(Long postId, String userEmail, CommentRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
        }

        Comment comment = Comment.create(post, user, parent, dto.getContent());
        Comment saved = commentRepository.save(comment);

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

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId) {
        List<Comment> list = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);

        return list.stream()
                .map(c -> CommentResponseDto.builder()
                        .id(c.getId())
                        .postId(c.getPost().getId())
                        .parentId(c.getParent() != null ? c.getParent().getId() : null)
                        .content(c.getContent())
                        .writerNickname(c.getUser().getNickname())
                        .writerEmail(c.getUser().getEmail())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();
    }
}
