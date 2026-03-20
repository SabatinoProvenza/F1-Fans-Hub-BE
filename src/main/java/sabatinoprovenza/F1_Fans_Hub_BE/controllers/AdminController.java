package sabatinoprovenza.F1_Fans_Hub_BE.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.CommentResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.dto.PostResponse;
import sabatinoprovenza.F1_Fans_Hub_BE.services.AdminService;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/posts")
    public Page<PostResponse> getAllPosts(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllPosts(username, page, size);
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable UUID postId) {
        adminService.deletePost(postId);
    }

    @GetMapping("/comments")
    public Page<CommentResponse> getAllComments(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getAllComments(username, page, size);
    }

    @GetMapping("/posts/{postId}/comments")
    public Page<CommentResponse> getCommentsByPost(
            @PathVariable UUID postId,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminService.getCommentsByPost(postId, username, page, size);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable UUID commentId) {
        adminService.deleteComment(commentId);

    }
}
