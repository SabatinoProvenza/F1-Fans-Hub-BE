package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record PostRequest(
        @NotBlank(message = "Il contenuto del post è obbligatorio")
        String content,
        MultipartFile image,
        Boolean removeImage
) {
}
