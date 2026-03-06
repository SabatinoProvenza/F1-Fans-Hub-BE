package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Favorite;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    List<Favorite> findByUser(User user);

    List<Favorite> findByUserId(UUID userId);

    Optional<Favorite> findByUserAndArticleId(User user, String articleId);

    boolean existsByUserAndArticleId(User user, String articleId);
}
