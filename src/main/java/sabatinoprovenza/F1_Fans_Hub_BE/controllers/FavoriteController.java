package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
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
}
