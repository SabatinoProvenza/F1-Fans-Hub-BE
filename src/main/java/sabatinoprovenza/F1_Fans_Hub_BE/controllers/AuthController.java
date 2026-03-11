package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.*;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.services.AuthService;
import sabatinoprovenza.F1_Fans_Hub_BE.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;


    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

    @PatchMapping("/me/username")
    public UserResponse updateUsername(
            @Valid
            @RequestBody UpdateUsernameRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return userService.updateUsername(request, currentUser);
    }

    @PatchMapping("/me/email")
    public UserResponse updateEmail(
            @Valid
            @RequestBody UpdateEmailRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return userService.updateEmail(request, currentUser);
    }

}

