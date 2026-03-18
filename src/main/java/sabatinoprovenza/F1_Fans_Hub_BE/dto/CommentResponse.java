package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String content,
        LocalDateTime createdAt,
        UUID postId,
        UUID userId,
        String username,
        String userImage
) {
}