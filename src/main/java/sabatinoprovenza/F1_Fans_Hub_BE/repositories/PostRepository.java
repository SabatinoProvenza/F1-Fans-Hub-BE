package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByOrderByCreatedAtDesc();
}
