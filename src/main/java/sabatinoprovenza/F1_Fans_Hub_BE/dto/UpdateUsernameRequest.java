package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUsernameRequest(
        @NotBlank(message = "L'username è obbligatorio")
        String username) {
}