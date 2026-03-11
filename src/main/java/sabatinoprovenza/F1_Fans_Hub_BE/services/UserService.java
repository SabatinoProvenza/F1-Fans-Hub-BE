package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdateEmailRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdateUsernameRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.UserRepository;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email: " + email + " non è stato trovato"));
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id: " + id + " non è stato trovato"));
    }

    public UserResponse updateUsername(UpdateUsernameRequest request, User currentUser) {

        boolean usernameExist = userRepository.existsByUsername(request.username());

        if (usernameExist && !currentUser.getUsername().equals(request.username())) {
            throw new BadRequestException("Username già in uso");
        }

        currentUser.setUsername(request.username());
        userRepository.save(currentUser);

        return new UserResponse(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getSurname(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getImage()
        );
    }

    public UserResponse updateEmail(UpdateEmailRequest request, User currentUser) {


        boolean emailExists = userRepository.existsByEmail(request.email());

        if (emailExists && !currentUser.getEmail().equals(request.email())) {
            throw new BadRequestException("Email già in uso");
        }

        currentUser.setEmail(request.email());
        userRepository.save(currentUser);

        return new UserResponse(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getSurname(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getImage()
        );
    }
}
