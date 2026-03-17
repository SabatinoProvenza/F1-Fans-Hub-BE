package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String content,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        UUID userId,
        String userUsername,
        String userImage,
        int likesCount,
        int commentsCount

) {
}
