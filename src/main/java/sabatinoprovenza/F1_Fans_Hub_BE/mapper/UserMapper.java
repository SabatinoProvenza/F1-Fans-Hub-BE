package sabatinoprovenza.F1_Fans_Hub_BE.mapper;

import org.springframework.stereotype.Component;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user.getDeletedAt() != null) {
            return new UserResponse(
                    user.getId(),
                    "Utente",
                    "Eliminato",
                    "utente_eliminato",
                    null,
                    "https://ui-avatars.com/api/?name=Utente+Eliminato&background=cccccc&color=555555",
                    user.getRole()
            );
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getEmail(),
                user.getImage(),
                user.getRole()
        );
    }
}
