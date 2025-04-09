package my_board.board.repository;

import my_board.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 기본적인 findAll(), save(), findById() 구성
}
