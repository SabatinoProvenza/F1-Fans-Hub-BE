package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.CommentRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.CommentResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Comment;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.UnauthorizedException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.CommentRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public CommentResponse createComment(UUID postId, CommentRequest request, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));


        Comment comment = new Comment(request.content(), post, currentUser);
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getPost().getId(),
                savedComment.getUser().getId(),
                savedComment.getUser().getUsername(),
                savedComment.getUser().getImage()
        );
    }

    public List<CommentResponse> getCommentsByPost(UUID postId) {

        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post non trovato");
        }

        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId);

        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getPost().getId(),
                        comment.getUser().getId(),
                        comment.getUser().getUsername(),
                        comment.getUser().getImage()
                ))
                .toList();
    }

    public CommentResponse patchComment(UUID commentId, CommentRequest request, User currentUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Commento non trovato"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Non puoi modificare questo commento");
        }

        if (request.content() != null && !request.content().isBlank()) {
            comment.setContent(request.content());
        }

        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponse(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getPost().getId(),
                updatedComment.getUser().getId(),
                updatedComment.getUser().getUsername(),
                updatedComment.getUser().getImage()
        );
    }

    public void deleteComment(UUID commentId, User currentUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Commento non trovato"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Non puoi eliminare questo commento");
        }

        commentRepository.delete(comment);
    }
}
