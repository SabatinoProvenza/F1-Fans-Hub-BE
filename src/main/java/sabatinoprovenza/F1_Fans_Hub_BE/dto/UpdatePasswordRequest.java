package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "La password attuale è obbligatoria")
        String currentPassword,

        @NotBlank(message = "La nuova password è obbligatoria")
        @Size(min = 6, message = "La password deve essere di almeno sei caratteri")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$", message = "La password deve contenere una maiuscola ed un numero")
        String newPassword,

        @NotBlank(message = "La conferma password è obbligatoria")
        String confirmPassword
) {
}
