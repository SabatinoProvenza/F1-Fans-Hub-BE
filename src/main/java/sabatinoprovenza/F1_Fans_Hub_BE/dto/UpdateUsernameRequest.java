package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUsernameRequest(
        @NotBlank(message = "L'username è obbligatorio")
        @Size(min = 1, max = 10, message = "L'username deve essere almeno 1 carattere e massimo 10")
        String username) {
}