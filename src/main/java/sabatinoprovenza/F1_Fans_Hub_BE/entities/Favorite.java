package sabatinoprovenza.F1_Fans_Hub_BE.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "favorites",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "article_id"})
        }
)
public class Favorite {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Favorite() {
    }

    public Favorite(User user, Article article) {
        this.savedAt = LocalDateTime.now();
        this.user = user;
        this.article = article;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public User getUser() {
        return user;
    }

    public Article getArticle() {
        return article;
    }
}
