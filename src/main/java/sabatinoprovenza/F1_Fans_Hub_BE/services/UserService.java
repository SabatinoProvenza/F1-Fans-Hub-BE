package sabatinoprovenza.F1_Fans_Hub_BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdateEmailRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdatePasswordRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdateUsernameRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.UserRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, Cloudinary cloudinary, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.passwordEncoder = passwordEncoder;
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

    public void updatePassword(User currentUser, UpdatePasswordRequest request) {


        if (!passwordEncoder.matches(request.currentPassword(), currentUser.getPassword())) {
            throw new BadRequestException("La password attuale non è corretta");
        }

        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new BadRequestException("Le password non coincidono");
        }

        if (passwordEncoder.matches(request.newPassword(), currentUser.getPassword())) {
            throw new BadRequestException("La nuova password non può essere uguale a quella attuale");
        }

        currentUser.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(currentUser);
    }

    public UserResponse uploadImage(User currentUser, MultipartFile file) {
        if (file.isEmpty()) throw new BadRequestException("Il file non può essere vuoto");

        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            currentUser.setImage(imageUrl);

            userRepository.save(currentUser);
            return new UserResponse(currentUser.getId(), currentUser.getName(), currentUser.getSurname(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getImage());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
