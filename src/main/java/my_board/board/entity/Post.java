// ✅ User.java (Entity)
package my_board.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String email;

    private String nickname;

    private LocalDateTime createAt;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }
}