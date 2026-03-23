package sabatinoprovenza.F1_Fans_Hub_BE.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.UserResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.Post;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.PostLike;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.BadRequestException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.NotFoundException;
import sabatinoprovenza.F1_Fans_Hub_BE.exceptions.UnauthorizedActionException;
import sabatinoprovenza.F1_Fans_Hub_BE.mapper.UserMapper;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostLikeRepository;
import sabatinoprovenza.F1_Fans_Hub_BE.repositories.PostRepository;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final Cloudinary cloudinary;
    private final PostLikeRepository postLikeRepository;
    private final UserMapper userMapper;

    public PostService(PostRepository postRepository, Cloudinary cloudinary, PostLikeRepository postLikeRepository, UserMapper userMapper) {
        this.postRepository = postRepository;
        this.cloudinary = cloudinary;
        this.postLikeRepository = postLikeRepository;
        this.userMapper = userMapper;
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

        UserResponse author = userMapper.toResponse(savedPost.getUser());

        return new PostResponse(
                savedPost.getId(),
                savedPost.getContent(),
                savedPost.getImageUrl(),
                savedPost.getCreatedAt(),
                savedPost.getUpdatedAt(),
                author.id(),
                author.username(),
                author.image(),
                savedPost.getLikes().size(),
                savedPost.getComments().size(),
                false
        );
    }

    public Page<PostResponse> getAllPosts(User currentUser, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Post> postsPage = postRepository.findAll(pageable);

        return postsPage.map(post -> {
            UserResponse author = userMapper.toResponse(post.getUser());

            return new PostResponse(
                    post.getId(),
                    post.getContent(),
                    post.getImageUrl(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    author.id(),
                    author.username(),
                    author.image(),
                    post.getLikes().size(),
                    post.getComments().size(),
                    currentUser != null && postLikeRepository.existsByPostAndUser(post, currentUser)
            );
        });
    }

    public void deletePost(UUID postId, User currentUser) {


        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("Non puoi eliminare questo post");
        }

        postRepository.delete(post);
    }

    public PostResponse patchPost(UUID postId, PostRequest request, User currentUser) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("Non puoi modificare questo post");
        }

        if (request.content() != null && !request.content().isBlank()) {
            post.setContent(request.content());
        }

        if (Boolean.TRUE.equals(request.removeImage())) {
            post.setImageUrl(null);
        }

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
        UserResponse author = userMapper.toResponse(updatedPost.getUser());

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getImageUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getUpdatedAt(),
                author.id(),
                author.username(),
                author.image(),
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

        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        UserResponse author = userMapper.toResponse(updatedPost.getUser());

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getImageUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getUpdatedAt(),
                author.id(),
                author.username(),
                author.image(),
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

        Post updatedPost = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post non trovato"));

        UserResponse author = userMapper.toResponse(updatedPost.getUser());

        return new PostResponse(
                updatedPost.getId(),
                updatedPost.getContent(),
                updatedPost.getImageUrl(),
                updatedPost.getCreatedAt(),
                updatedPost.getUpdatedAt(),
                author.id(),
                author.username(),
                author.image(),
                updatedPost.getLikes().size(),
                updatedPost.getComments().size(),
                false
        );
    }
}
