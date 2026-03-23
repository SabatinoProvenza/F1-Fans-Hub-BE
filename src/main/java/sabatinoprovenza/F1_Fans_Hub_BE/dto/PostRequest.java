package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostRequest(
        @NotBlank(message = "Il contenuto del post è obbligatorio")
        @Size(max = 500, message = "Il post non può superare i 500 caratteri")
        String content,
        MultipartFile image,
        Boolean removeImage
) {
    public String content() {
        return content == null ? null : content.trim();
    }
}
