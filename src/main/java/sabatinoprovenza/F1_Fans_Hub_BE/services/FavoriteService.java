package sabatinoprovenza.F1_Fans_Hub_BE.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.ArticleResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.FavoriteResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Article;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Favorite;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.ArticleRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.FavoriteRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final NewsService newsService;


    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, ArticleRepository articleRepository, NewsService newsService) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.newsService = newsService;
    }

    public ArticleResponse getNewsByIdWithFavorite(String id, UUID userId) {
        return getNewsWithFavorite(userId).stream()
                .filter(article -> article.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Articolo non trovato"));
    }

    public List<ArticleResponse> getNewsWithFavorite(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        Set<String> favoriteIds = favoriteRepository.findByUser(user).stream()
                .map(favorite -> favorite.getArticle().getGuid())
                .collect(Collectors.toSet());

        return newsService.getNews().stream()
                .map(article -> new ArticleResponse(
                        article.id(),
                        article.title(),
                        article.description(),
                        article.content(),
                        article.image(),
                        article.link(),
                        article.pubDate(),
                        article.source(),
                        favoriteIds.contains(article.id())
                ))
                .toList();
    }

    public List<FavoriteResponse> getFavorites(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        return favoriteRepository.findByUserOrderBySavedAtDesc(user).stream()
                .map(favorite -> {
                    Article article = favorite.getArticle();

                    return new FavoriteResponse(
                            article.getGuid(),
                            article.getTitle(),
                            article.getDescription(),
                            article.getContent(),
                            article.getImageUrl(),
                            article.getLink(),
                            article.getPubDate(),
                            article.getSource(),
                            true,
                            favorite.getSavedAt()
                    );
                })
                .toList();
    }

    @Transactional
    public void removeFavorite(User currentUser, String articleId) {


        Article article = articleRepository.findByGuid(articleId)
                .orElseThrow(() -> new NotFoundException("Articolo non trovato"));

        if (!favoriteRepository.existsByUserAndArticle(currentUser, article)) {
            throw new NotFoundException("Preferito non trovato");
        }

        favoriteRepository.deleteByUserAndArticle(currentUser, article);

        if (!favoriteRepository.existsByArticle(article)) {
            articleRepository.delete(article);
        }
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
                article.getSource(),
                true
        );
    }
}
