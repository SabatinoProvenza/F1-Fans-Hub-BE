package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.LoginDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.LoginResponseDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.RegisterDTO;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;

    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid RegisterDTO dto) {
        return this.authService.userRegister(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO dto) {
        return this.authService.UserLogin(dto);
    }

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal User currentUser) {
        return this.authService.getCurrentUser(currentUser);
    }

}

