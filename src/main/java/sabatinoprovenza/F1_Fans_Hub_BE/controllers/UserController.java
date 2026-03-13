package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.*;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.services.AuthService;
import sabatinoprovenza.F1_Fans_Hub_BE.services.FavoriteService;
import sabatinoprovenza.F1_Fans_Hub_BE.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final FavoriteService favoriteService;

    public UserController(AuthService authService, UserService userService, FavoriteService favoriteService) {
        this.authService = authService;
        this.userService = userService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal User currentUser) {
        return this.authService.getCurrentUser(currentUser);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@AuthenticationPrincipal User currentUser) {
        userService.deleteUser(currentUser.getId());
    }

    @GetMapping("/me/news/{id}")
    public ArticleResponse getNewsDetailPrefs(@PathVariable String id,
                                              @AuthenticationPrincipal User currentUser) {
        return this.favoriteService.getNewsByIdWithFavorite(id, currentUser.getId());
    }

    @PatchMapping("/me/username")
    public UserResponse updateUsername(
            @Valid @RequestBody UpdateUsernameRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return userService.updateUsername(request, currentUser);
    }

    @PatchMapping("/me/email")
    public UserResponse updateEmail(
            @Valid @RequestBody UpdateEmailRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return userService.updateEmail(request, currentUser);
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdatePasswordRequest request
    ) {
        userService.updatePassword(user, request);
    }

    @PatchMapping("/me/image")
    public UserResponse updateImage(
            @RequestParam("image") MultipartFile file,
            @AuthenticationPrincipal User currentUser
    ) {
        return userService.uploadImage(currentUser, file);
    }
}
