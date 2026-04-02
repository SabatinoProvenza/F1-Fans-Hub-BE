package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.Instant;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String content,
        String imageUrl,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        String userUsername,
        String userImage,
        int likesCount,
        int commentsCount,
        boolean likedByCurrentUser

) {
}
