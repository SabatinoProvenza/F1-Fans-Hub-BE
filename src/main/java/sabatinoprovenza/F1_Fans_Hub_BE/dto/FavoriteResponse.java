package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoriteResponse(
        UUID articleId,
        String guid,
        String title,
        String description,
        String content,
        String image,
        String link,
        String pubDate,
        String source,
        boolean isFavorite,
        LocalDateTime savedAt
) {
}