package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.LoginDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.LoginResponseDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.RegisterDTO;
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

    public User UserRegister(RegisterDTO dto) {

        if (this.userService.existByEmail(dto.email())) {
            throw new BadRequestException("Email già in uso!");
        }
        User u = new User();
        u.setFullName(dto.fullName());
        u.setEmail(dto.email());
        u.setPassword(bcrypt.encode(dto.password()));

        return this.userRepository.save(u);
    }

    public LoginResponseDTO UserLogin(LoginDTO dto) {

        User found = this.userService.findByEmail(dto.email());

        if (bcrypt.matches(dto.password(), found.getPassword())) {

            String token = jwtTools.generateToken(found);
            return new LoginResponseDTO(token);

        } else {
            throw new UnauthorizedException("Le credenziali inserite sono errate!");
        }
    }
}
