package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.FavoriteResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.services.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse addFavorite(
            @AuthenticationPrincipal User currentUser,
            @RequestBody ArticleResponse articleResponse
    ) {

        return favoriteService.addFavorite(currentUser.getId(), articleResponse);


    }

    @Validated
    @GetMapping
    public Page<FavoriteResponse> getFavorites(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) int size,
            @RequestParam(defaultValue = "savedAt")
            @Pattern(regexp = "savedAt|pubDate")
            String sort,
            @RequestParam(defaultValue = "desc")
            @Pattern(regexp = "asc|desc")
            String direction
    ) {
        return favoriteService.getFavorites(currentUser.getId(), page, size, sort, direction);
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String articleId
    ) {
        favoriteService.removeFavorite(currentUser, articleId);
    }

}
