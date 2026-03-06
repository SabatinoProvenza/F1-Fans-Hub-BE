package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank(message = "L'email è obbligatoria")
        @Email
        String email,
        @NotBlank(message = "Password obbligatoria")
        String password
) {
}
