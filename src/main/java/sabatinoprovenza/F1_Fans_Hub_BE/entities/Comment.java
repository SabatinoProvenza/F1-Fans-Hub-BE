package sabatinoprovenza.F1_Fans_Hub_BE.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment() {
    }

    public Comment(String content, Post post, User user) {
        this.post = post;
        this.user = user;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }
}