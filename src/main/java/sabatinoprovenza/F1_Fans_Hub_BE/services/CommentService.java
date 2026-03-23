package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.CommentRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.CommentResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Comment;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.UnauthorizedActionException;
import sabatinoprovenza.F1_Fans_Hub_BE.mapper.UserMapper;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.CommentRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostRepository;

import java.util.UUID;

@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserMapper userMapper;

    public CommentService(PostRepository postRepository, CommentRepository commentRepository, UserMapper userMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userMapper = userMapper;
    }

    public CommentResponse createComment(UUID postId, CommentRequest request, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        Comment comment = new Comment(request.content(), post, currentUser);
        Comment savedComment = commentRepository.save(comment);

        UserResponse author = userMapper.toResponse(savedComment.getUser());

        return new CommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt(),
                savedComment.getPost().getId(),
                author.id(),
                author.username(),
                author.image()
        );
    }

    public Page<CommentResponse> getCommentsByPost(UUID postId, int page, int size) {

        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post non trovato");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Comment> commentsPage = commentRepository.findByPostId(postId, pageable);

        return commentsPage.map(comment -> {
            UserResponse author = userMapper.toResponse(comment.getUser());

            return new CommentResponse(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedAt(),
                    comment.getPost().getId(),
                    author.id(),
                    author.username(),
                    author.image()
            );
        });
    }

    public CommentResponse patchComment(UUID commentId, CommentRequest request, User currentUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Commento non trovato"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("Non puoi modificare questo commento");
        }

        if (request.content() != null && !request.content().isBlank()) {
            comment.setContent(request.content());
        }

        Comment updatedComment = commentRepository.save(comment);
        UserResponse author = userMapper.toResponse(updatedComment.getUser());

        return new CommentResponse(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getCreatedAt(),
                updatedComment.getPost().getId(),
                author.id(),
                author.username(),
                author.image()
        );
    }

    public void deleteComment(UUID commentId, User currentUser) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Commento non trovato"));

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("Non puoi eliminare questo commento");
        }

        commentRepository.delete(comment);
    }
}
