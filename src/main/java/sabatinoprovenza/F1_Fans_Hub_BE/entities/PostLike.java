package sabatinoprovenza.F1_Fans_Hub_BE.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        }
)
public class PostLike {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostLike() {
    }

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
