package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.Instant;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String content,
        Instant createdAt,
        UUID postId,
        UUID userId,
        String username,
        String userImage
) {
}