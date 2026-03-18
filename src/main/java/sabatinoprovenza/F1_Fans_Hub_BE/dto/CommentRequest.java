package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "Il commento non può essere vuoto")
        String content
) {
}
