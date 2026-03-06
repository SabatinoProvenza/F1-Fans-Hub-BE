package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "Il nome completo è obbligatorio")
        String fullName,
        @NotBlank(message = "L'email è obbligatoria")
        @Email
        String email,
        @NotBlank
        @Size(min = 6, message = "La password deve essere di almeno sei caratteri")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$", message = "La password deve contenere una maiuscola ed un numero")
        String password
) {
}
