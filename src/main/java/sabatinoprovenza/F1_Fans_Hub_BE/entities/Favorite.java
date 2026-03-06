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

    @Column(name = "article_id", nullable = false)
    private String articleId;

    @Column(name = "article_title", nullable = false)
    private String articleTitle;

    @Column(name = "article_url", nullable = false)
    private String articleUrl;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Favorite() {
    }

    public Favorite(String articleId, String articleTitle, String articleUrl, String imageUrl, LocalDateTime savedAt, User user) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleUrl = articleUrl;
        this.imageUrl = imageUrl;
        this.savedAt = savedAt;
        this.user = user;
    }

}
