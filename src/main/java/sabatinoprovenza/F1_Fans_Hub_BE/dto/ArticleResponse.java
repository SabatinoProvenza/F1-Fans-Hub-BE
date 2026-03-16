package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.util.UUID;

public record ArticleResponse(
        UUID articleId,
        String guid,
        String title,
        String description,
        String content,
        String image,
        String link,
        String pubDate,
        String source,
        boolean isFavorite
) {
}
