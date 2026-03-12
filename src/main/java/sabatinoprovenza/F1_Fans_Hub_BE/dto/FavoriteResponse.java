package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.LocalDateTime;

public record FavoriteResponse(
        String id,
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