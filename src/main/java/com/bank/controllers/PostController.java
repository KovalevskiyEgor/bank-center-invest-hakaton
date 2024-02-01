package com.bank.controllers;

import com.bank.dto.ImageDTO;
import com.bank.dto.PostDTO;
import com.bank.models.Image;
import com.bank.models.Post;
import com.bank.service.PostService;
import com.bank.utils.enums.PostType;
import com.bank.utils.mappers.impl.ImageMapper;
import com.bank.utils.mappers.impl.PostMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
@Validated
@Tag(name = "Post Controller", description = "Post API")
public class PostController extends MainController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final ImageMapper imageMapper;

    @Operation(summary = "Get all posts")
    @GetMapping
    public ResponseEntity<Object> getAllPosts(){
        return ResponseEntity
                .ok()
                .body(postMapper.toDTOs(postService.getAll()));
    }

    @Operation(summary = "Get post by id")
    @GetMapping("/{post_id}")
    public ResponseEntity<Object> getById(@PathVariable("post_id") Long id){
        return ResponseEntity
                .ok()
                .body(postMapper.toDTO(postService.getById(id)));
    }

    @PreAuthorize("@customSecurityExpression.isPostOwner(#postId)")
    @Operation(summary = "Delete post by id")
    @DeleteMapping("/{post_id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("post_id") Long postId){
        postService.deleteById(postId);
    }

    @Operation(summary = "Create post")
    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@RequestBody @Valid PostDTO postDTO, BindingResult bindingResult){
        checkBindingResult(bindingResult);
        Post post = postMapper.fromDTO(postDTO);
        return ResponseEntity
                .ok()
                .body(postMapper.toDTO(postService.save(post)));
    }

    @Operation(summary = "Update existing post")
    @PreAuthorize("@customSecurityExpression.isPostOwner(#postId)")
    @PatchMapping("/{post_id}/update")
    public ResponseEntity<Object> updatePost(
            @PathVariable("post_id") Long postId,
            @Valid @RequestBody PostDTO postDTO){
        postDTO.setId(postId);
        Post post = postMapper.fromDTO(postDTO);
        return ResponseEntity.
                ok()
                .body(postMapper.toDTO(postService.update(post)));
    }

    @Operation(summary = "Get all event posts")
    @GetMapping("/events")
    public ResponseEntity<Object> getAllEvents(){
        return ResponseEntity
                .ok()
                .body(postMapper.toDTOs(postService.getAllByType(PostType.EVENT)));
    }

    @Operation(summary = "Get all landmark posts")
    @GetMapping("/landmarks")
    public ResponseEntity<Object> getAllLandmarks(){
        return ResponseEntity
                .ok()
                .body(postMapper.toDTOs(postService.getAllByType(PostType.LANDMARK)));
    }

    @Operation(summary = "Get all images for post by post id")
    @GetMapping("/{post_id}/images")
    public ResponseEntity<Object> getAllImagesForPost(@PathVariable("post_id") Long postId) {
        return new ResponseEntity<>(postService.getById(postId).getImages(), HttpStatus.OK);
    }

    @Operation(summary = "Add image for post by post id")
    @PostMapping("/{post_id}/images/add")
    @PreAuthorize("@customSecurityExpression.isPostOwner(#postId)")
    public ResponseEntity<Object> uploadImageForPost(@PathVariable("post_id") Long postId,
                                                     @ModelAttribute ImageDTO imageDTO) {
        Image image = imageMapper.fromDTO(imageDTO);
        postService.uploadImage(postId, image);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Delete image for post by post id")
    @DeleteMapping("/{post_id}/images/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@customSecurityExpression.isPostOwner(#postId)")
    public void deleteImage(@PathVariable("post_id") Long postId,
                            @RequestBody String jsonRequest) {
        JSONObject jsonObject = new JSONObject(jsonRequest);
        String name = jsonObject.getString("name");
        postService.deleteImage(postId, name);
    }

}
