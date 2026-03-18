package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.PostLike;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {

    boolean existsByPostAndUser(Post post, User user);

    Optional<PostLike> findByPostAndUser(Post post, User user);

}
