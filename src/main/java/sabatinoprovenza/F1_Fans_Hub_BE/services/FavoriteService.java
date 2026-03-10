package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Article;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Favorite;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.ArticleRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.FavoriteRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.UserRepository;

import java.util.UUID;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    public ArticleResponse addFavorite(UUID userId, ArticleResponse articleResponse) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Article article = articleRepository.findByGuid(articleResponse.id())
                .orElseGet(() -> {
                    Article newArticle = new Article(
                            articleResponse.id(),
                            articleResponse.title(),
                            articleResponse.description(),
                            articleResponse.content(),
                            articleResponse.image(),
                            articleResponse.link(),
                            articleResponse.pubDate(),
                            articleResponse.source()
                    );
                    return articleRepository.save(newArticle);
                });

        if (!favoriteRepository.existsByUserAndArticle(user, article)) {
            Favorite favorite = new Favorite(user, article);
            favoriteRepository.save(favorite);
        }

        return new ArticleResponse(
                article.getGuid(),
                article.getTitle(),
                article.getDescription(),
                article.getContent(),
                article.getImageUrl(),
                article.getLink(),
                article.getPubDate(),
                article.getSource()
        );
    }
}
