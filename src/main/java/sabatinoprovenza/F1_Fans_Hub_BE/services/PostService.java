package sabatinoprovenza.F1_Fans_Hub_BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.PostLike;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.UnauthorizedException;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostLikeRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final Cloudinary cloudinary;
    private final PostLikeRepository postLikeRepository;

    public PostService(PostRepository postRepository, Cloudinary cloudinary, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.cloudinary = cloudinary;
        this.postLikeRepository = postLikeRepository;
    }

    public PostResponse createPost(PostRequest request, User user) {

        String imageUrl = null;
        MultipartFile file = request.image();

        try {
            if (file != null && !file.isEmpty()) {
                Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                imageUrl = (String) result.get("secure_url");
            }

        } catch (IOException e) {
            throw new RuntimeException("Errore upload immagine", e);
        }

        Post post = new Post(request.content(), imageUrl);
        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return new PostResponse(
                savedPost.getId(),
                savedPost.getContent(),
                savedPost.getImageUrl(),
                savedPost.getCreatedAt(),
                savedPost.getUpdatedAt(),
                savedPost.getUser().getId(),
                savedPost.getUser().getUsername(),
                savedPost.getUser().getImage(),
                savedPost.getLikes().size(),
                savedPost.getComments().size(),
                false
        );
    }

    public List<PostResponse> getAllPosts(User currentUser) {

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        return posts.stream()
                .map(post -> new PostResponse(
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
                        currentUser != null && postLikeRepository.existsByPostAndUser(post, currentUser)
                ))
                .toList();
    }

    public void deletePost(UUID postId, User currentUser) {


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Non puoi eliminare questo post");
        }

        postRepository.delete(post);
    }

    public PostResponse patchPost(UUID postId, PostRequest request, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Non puoi modificare questo post");
        }

        // Aggiorna content solo se presente e non vuoto
        if (request.content() != null && !request.content().isBlank()) {
            post.setContent(request.content());
        }

        if (Boolean.TRUE.equals(request.removeImage())) {
            post.setImageUrl(null);
        }

        // Aggiorna immagine solo se arriva un file
        MultipartFile file = request.image();

        try {
            if (file != null && !file.isEmpty()) {
                Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) result.get("secure_url");
                post.setImageUrl(imageUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("Errore upload immagine", e);
        }

        Post updatedPost = postRepository.save(post);

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getImageUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getUpdatedAt(),
                updatedPost.getUser().getId(),
                updatedPost.getUser().getUsername(),
                updatedPost.getUser().getImage(),
                updatedPost.getLikes().size(),
                updatedPost.getComments().size(),
                postLikeRepository.existsByPostAndUser(updatedPost, currentUser)
        );
    }

    public PostResponse likePost(UUID postId, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        boolean alreadyLiked = postLikeRepository.existsByPostAndUser(post, currentUser);

        if (alreadyLiked) {
            throw new BadRequestException("Hai già messo like a questo post");
        }

        PostLike postLike = new PostLike(post, currentUser);
        postLikeRepository.save(postLike);

        // ricarico il post aggiornato
        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getImageUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getUpdatedAt(),
                updatedPost.getUser().getId(),
                updatedPost.getUser().getUsername(),
                updatedPost.getUser().getImage(),
                updatedPost.getLikes().size(),
                updatedPost.getComments().size(),
                true
        );
    }

    public PostResponse unlikePost(UUID postId, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        PostLike postLike = postLikeRepository.findByPostAndUser(post, currentUser)
                .orElseThrow(() -> new BadRequestException("Non hai ancora messo like a questo post"));

        postLikeRepository.delete(postLike);

        // ricarico il post aggiornato
        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getImageUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getUpdatedAt(),
                updatedPost.getUser().getId(),
                updatedPost.getUser().getUsername(),
                updatedPost.getUser().getImage(),
                updatedPost.getLikes().size(),
                updatedPost.getComments().size(),
                false
        );
    }
}
