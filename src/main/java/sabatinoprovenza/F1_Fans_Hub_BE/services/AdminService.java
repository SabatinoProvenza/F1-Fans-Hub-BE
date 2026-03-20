package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.CommentResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Comment;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.CommentRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostRepository;

import java.util.UUID;

@Service
public class AdminService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public AdminService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Page<PostResponse> getAllPosts(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> posts;

        if (username == null || username.isBlank()) {
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findByUserUsernameContainingIgnoreCase(username, pageable);
        }

        return posts.map(post -> new PostResponse(
                post.getId(),
                post.getContent(),
                post.getImageUrl(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getUser().getImage(),
                post.getLikes().size(),
                post.getComments().size(),
                false
        ));
    }

    public Page<CommentResponse> getAllComments(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Comment> comments;

        if (username == null || username.isBlank()) {
            comments = commentRepository.findAll(pageable);
        } else {
            comments = commentRepository.findByUserUsernameContainingIgnoreCase(username, pageable);
        }

        return comments.map(comment -> new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getImage()
        ));
    }

    public Page<CommentResponse> getCommentsByPost(UUID postId, String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Comment> comments;

        if (username == null || username.isBlank()) {
            comments = commentRepository.findByPostId(postId, pageable);
        } else {
            comments = commentRepository.findByPostIdAndUserUsernameContainingIgnoreCase(postId, username, pageable);
        }

        return comments.map(comment -> new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getPost().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getUser().getImage()
        ));
    }

    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }

    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }
}
