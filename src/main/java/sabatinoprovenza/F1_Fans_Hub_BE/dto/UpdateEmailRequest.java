package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailRequest(
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Formato dell'email non valido")
        String email) {
}
