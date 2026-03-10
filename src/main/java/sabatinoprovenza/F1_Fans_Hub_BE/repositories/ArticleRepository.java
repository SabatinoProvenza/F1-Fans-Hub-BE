package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Article;

import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
    Optional<Article> findByGuid(String guid);
}
