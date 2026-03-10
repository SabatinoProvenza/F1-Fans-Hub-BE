package sabatinoprovenza.F1_Fans_Hub_BE.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "articles",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "guid")
        })
public class Article {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String guid;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column
    private String pubDate;

    @Column
    private String link;

    @Column
    private String source;


    public Article() {
    }

    public Article(String guid, String title, String description, String content, String imageUrl, String link, String pubDate, String source) {
        this.guid = guid;
        this.title = title;
        this.description = description;
        this.content = content;
        this.imageUrl = imageUrl;
        this.link = link;
        this.pubDate = pubDate;
        this.source = source;
    }

    public UUID getId() {
        return id;
    }

    public String getGuid() {
        return guid;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getLink() {
        return link;
    }

    public String getSource() {
        return source;
    }
}
