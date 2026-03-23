package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Il commento non può essere vuoto")
        @Size(max = 150, message = "Il commento non può superare i 150 caratteri")
        String content
) {
    public String content() {
        return content == null ? null : content.trim();
    }
}
