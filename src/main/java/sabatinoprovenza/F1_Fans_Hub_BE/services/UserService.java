package sabatinoprovenza.F1_Fans_Hub_BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdateEmailRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdatePasswordRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UpdateUsernameRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Favorite;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.UnauthorizedException;
import sabatinoprovenza.F1_Fans_Hub_BE.mapper.UserMapper;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.FavoriteRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteService favoriteService;
    private final FavoriteRepository favoriteRepository;
    private final UserMapper userMapper;
    private final ImageValidationService imageValidationService;

    public UserService(UserRepository userRepository, Cloudinary cloudinary, PasswordEncoder passwordEncoder, FavoriteService favoriteService, FavoriteRepository favoriteRepository, UserMapper userMapper, ImageValidationService imageValidationService) {
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
        this.passwordEncoder = passwordEncoder;
        this.favoriteService = favoriteService;
        this.favoriteRepository = favoriteRepository;
        this.userMapper = userMapper;
        this.imageValidationService = imageValidationService;
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

    public User findByEmailForLogin(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UnauthorizedException("Le credenziali inserite sono errate!"));
    }

    public User findActiveUserById(UUID userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UnauthorizedException("Utente non autenticato"));
    }

    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        List<Favorite> favorites = favoriteRepository.findByUser(user);

        for (Favorite favorite : favorites) {
            favoriteService.removeFavorite(user, favorite.getArticle().getGuid());
        }

        String shortId = user.getId().toString().substring(0, 6);

        user.setDeletedAt(LocalDateTime.now());
        user.setUsername("del_" + shortId);
        user.setEmail("deleted_" + user.getId() + "@deleted.local");
        userRepository.save(user);
    }

    public UserResponse updateUsername(UpdateUsernameRequest request, User currentUser) {
        String username = request.username().trim().toLowerCase();
        boolean usernameExist = userRepository.existsByUsername(username);

        if (usernameExist && !currentUser.getUsername().equalsIgnoreCase(username)) {
            throw new BadRequestException("Username già in uso");
        }

        currentUser.setUsername(username);
        userRepository.save(currentUser);

        return userMapper.toResponse(currentUser);
    }

    public UserResponse updateEmail(UpdateEmailRequest request, User currentUser) {
        String email = request.email().trim().toLowerCase();

        boolean emailExists = userRepository.existsByEmail(email);

        if (emailExists && !currentUser.getEmail().equals(email)) {
            throw new BadRequestException("Email già in uso");
        }

        currentUser.setEmail(email);
        userRepository.save(currentUser);

        return userMapper.toResponse(currentUser);
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
            imageValidationService.validateImage(file);
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imageUrl = (String) result.get("secure_url");

            currentUser.setImage(imageUrl);

            userRepository.save(currentUser);
            return new UserResponse(currentUser.getId(), currentUser.getName(), currentUser.getSurname(), currentUser.getUsername(), currentUser.getEmail(), currentUser.getImage(), currentUser.getRole());

        } catch (IOException e) {
            throw new BadRequestException("Errore upload immagine");
        }
    }

}
