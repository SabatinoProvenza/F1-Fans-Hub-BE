package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.LoginDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.LoginResponseDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.RegisterDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.UnauthorizedException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.UserRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.security.JWTTools;

@Service
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;
    private final EmailService emailService;

    public AuthService(UserService userService, UserRepository userRepository, JWTTools jwtTools, PasswordEncoder bcrypt, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
        this.emailService = emailService;
    }

    public UserResponse userRegister(RegisterDTO dto) {

        String email = dto.email().trim().toLowerCase();
        String username = dto.username().trim().toLowerCase();

        if (userService.existByEmail(email))
            throw new BadRequestException("Email già in uso!");

        if (userService.existByUsername(username))
            throw new BadRequestException("Username già in uso!");

        String normalizedName = normalizePersonName(dto.name());
        String normalizedSurname = normalizePersonName(dto.surname());

        User u = userRepository.save(new User(
                username, normalizedName, normalizedSurname, email, bcrypt.encode(dto.password())
        ));

        try {
            emailService.sendWelcomeEmail(u.getEmail(), u.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Invio email di benvenuto fallito: " + e.getMessage());
        }

        return new UserResponse(u.getId(), u.getName(), u.getSurname(), u.getUsername(), u.getEmail(), u.getImage(), u.getRole());
    }

    public LoginResponseDTO userLogin(LoginDTO dto) {
        User found = this.userService.findByEmailForLogin(dto.email());

        if (!bcrypt.matches(dto.password(), found.getPassword())) {
            throw new UnauthorizedException("Le credenziali inserite sono errate!");
        }

        String token = jwtTools.generateToken(found);
        return new LoginResponseDTO(token);
    }

    public UserResponse getCurrentUser(User currentUser) {
        if (currentUser == null || currentUser.getDeletedAt() != null) {
            throw new UnauthorizedException("Utente non autenticato");
        }

        return new UserResponse(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getSurname(),
                currentUser.getUsername(),
                currentUser.getEmail(),
                currentUser.getImage(),
                currentUser.getRole()

        );
    }

    private String normalizePersonName(String value) {
        if (value == null || value.isBlank()) return value;

        String[] parts = value.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String word = parts[i];
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
                if (i < parts.length - 1) {
                    result.append(" ");
                }
            }
        }

        return result.toString();
    }
}
