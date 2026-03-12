package sabatinoprovenza.F1_Fans_Hub_BE.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "Il nome è obbligatorio")
        String name,
        @NotBlank(message = "Il cognome è obbligatorio")
        String surname,
        @NotBlank(message = "L'username è obbligatorio")
        String username,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "Formato dell'email non valido")
        String email,
        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve essere di almeno sei caratteri")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{6,}$", message = "La password deve contenere una maiuscola ed un numero")
        String password
) {
}
