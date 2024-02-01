package com.bank.service;


import com.bank.exceptions.ResourceNotFoundException;
import com.bank.models.Image;
import com.bank.models.Post;
import com.bank.models.Review;
import com.bank.models.User;
import com.bank.repositories.PostRepository;
import com.bank.utils.enums.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final LocationService locationService;
    private final ImageService imageService;
    private final UserService userService;

    public boolean isPostOwner(User user){
        return postRepository.existsPostByUser(user);
    }

    public Post getById(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Post with this id not found"));
    }

    public Post getByTitle(String title){
        return postRepository.findByTitle(title).orElseThrow(() ->
                new ResourceNotFoundException("Post with this title not found"));
    }

    public List<Post> getAll(){
        return postRepository.findAll();
    }

    public List<Post> getAllByTitle(String title){
        return postRepository.findAllByTitle(title);
    }

    public List<Post> getAllByType(PostType postType){
        return postRepository.findAllByType(postType);
    }
    @Transactional
    public Post save(Post post){
        post.setDateOfPublish(Instant.now());
        post.setLocation(locationService.save(post.getLocation().getAddress()));
        post.setUser(userService.getAuthorizedUser());
        post.setRating(0.0);
        return postRepository.save(post);
    }

    @Transactional
    public Post deleteById(Long id){
        Post post = getById(id);
        postRepository.delete(post);
        return post;
    }

    @Transactional
    public Post update(Post post){
        post.setLocation(locationService.save(post.getLocation().getAddress()));
        return postRepository.save(post);
    }

    @Transactional
    public Post uploadImage(Long id, Image image) {
        Post post = getById(id);
        String imageName = imageService.upload(image);
        List<String> images = post.getImages();
        images.add(imageName);
        post.setImages(images);
        return postRepository.save(post);
    }

    @Transactional
    public void deleteImage(Long id, String imageName){
        Post post = getById(id);
        List<String> images = post.getImages();
        if (!images.remove(imageName))
            throw new ResourceNotFoundException("Image with this name not found for this post");
        imageService.removeImage(imageName);
        postRepository.save(post);
    }

    @Transactional
    public void updateRating(Post post, Review review){
        post.setTotalStars(post.getTotalStars()+review.getStars());
        post.setReviewCounter(post.getReviewCounter()+1);
        postRepository.save(post);
    }
}
