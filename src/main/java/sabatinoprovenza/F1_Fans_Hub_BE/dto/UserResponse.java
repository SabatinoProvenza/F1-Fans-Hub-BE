package sabatinoprovenza.F1_Fans_Hub_BE.dto;


import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String surname,
        String username,
        String email,
        String image
) {

}
