package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
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

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con email: " + email + " non è stato trovato"));
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("L'utente con id: " + id + " non è stato trovato"));
    }
}
