package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Article;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Favorite;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByUser(User user);

    void deleteByUserAndArticle(User user, Article article);

    Optional<Favorite> findByUserAndArticle(User user, Article article);

    boolean existsByUserAndArticle(User user, Article article);
}
