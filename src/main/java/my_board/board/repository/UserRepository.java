// ✅ UserRepository.java
package my_board.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<my_board.board.entity.User, Long> {
    Optional<my_board.board.entity.User> findByEmail(String email);
}
