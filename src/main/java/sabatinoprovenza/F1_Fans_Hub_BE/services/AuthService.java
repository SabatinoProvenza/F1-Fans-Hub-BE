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

    public AuthService(UserService userService, UserRepository userRepository, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public UserResponse userRegister(RegisterDTO dto) {
        if (userService.existByEmail(dto.email()))
            throw new BadRequestException("Email già in uso!");

        if (userService.existByUsername(dto.username()))
            throw new BadRequestException("Username già in uso!");

        User u = userRepository.save(new User(
                dto.username(), dto.name(), dto.surname(), dto.email(), bcrypt.encode(dto.password())
        ));

        return new UserResponse(u.getId(), u.getName(), u.getSurname(), u.getUsername(), u.getEmail(), u.getImage());
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
        if (currentUser == null) {
            throw new UnauthorizedException("Utente non autenticato");
        }

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
