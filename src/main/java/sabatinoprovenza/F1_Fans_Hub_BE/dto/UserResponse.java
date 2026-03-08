package sabatinoprovenza.F1_Fans_Hub_BE.dto;


import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String surname,
        String username,
        String email
) {
    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String surname() {
        return surname;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String email() {
        return email;
    }
}
