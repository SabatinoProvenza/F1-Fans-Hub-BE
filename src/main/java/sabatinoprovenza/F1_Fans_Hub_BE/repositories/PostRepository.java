package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findByUserUsernameContainingIgnoreCase(String username, Pageable pageable);
}
