package sabatinoprovenza.F1_Fans_Hub_BE.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Comment;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findByPostIdOrderByCreatedAtDesc(UUID postId);
}
