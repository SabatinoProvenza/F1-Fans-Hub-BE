package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostRequest;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.entities.User;
import sabatinoprovenza.F1_Fans_Hub_BE.services.PostService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(
            @Valid @ModelAttribute PostRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return postService.createPost(request, currentUser);

    }

    @GetMapping
    public List<PostResponse> getAllPosts(
            @AuthenticationPrincipal User currentUser
    ) {
        return postService.getAllPosts(currentUser);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User currentUser) {
        postService.deletePost(postId, currentUser);
    }

    @PatchMapping("/{postId}")
    public PostResponse patchPost(
            @PathVariable UUID postId,
            @Valid @ModelAttribute PostRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return postService.patchPost(postId, request, currentUser);
    }

    @PostMapping("/{postId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse likePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User currentUser
    ) {
        return postService.likePost(postId, currentUser);
    }

    @DeleteMapping("/{postId}/like")
    public PostResponse unlikePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User currentUser
    ) {
        return postService.unlikePost(postId, currentUser);
    }
}
