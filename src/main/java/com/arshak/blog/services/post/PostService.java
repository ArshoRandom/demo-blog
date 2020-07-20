package com.arshak.blog.services.post;

import com.arshak.blog.models.Post;
import com.arshak.blog.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Post getById(Integer id);

    void addPost(Post post, MultipartFile poster);

    List<Post> getAllPosts();

    List<Post> getAllPostsByUserId(Integer userId);

    List<Post> getNewPosts(int limit);

    List<Post> getNewPosts();

    void updatePost(Post newPost, Post oldPost, MultipartFile poster);

    void deletePostsById(Integer id);





}
