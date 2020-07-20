package com.arshak.blog.services.post;

import com.arshak.blog.exceptions.ServiceLayerException;
import com.arshak.blog.models.Post;
import com.arshak.blog.repository.PostRepository;
import lombok.SneakyThrows;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Scope("prototype")
@CacheConfig(cacheNames = "posts")
public class PostServiceImpl implements PostService {

    private final PostRepository repository;

    public PostServiceImpl(PostRepository repository) {
        this.repository = repository;
    }


    @Override
    public Post getById(Integer id) {
        try {
            Optional<Post> optionalPost = this.repository.findById(id);
            if (optionalPost.isPresent()) {
                return optionalPost.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }


    @Override
    public void addPost(Post post, MultipartFile poster) {
        try {
            if (post != null) {

                if (!poster.isEmpty()) {
                    post.setImage(new String(Base64.encodeBase64(poster.getBytes())));
                } else {
                    post.setImage(new String(Base64.encodeBase64(Files.readAllBytes(Paths.get("src/main/resources/static/images/blog/default.jpg")))));
                }
                this.repository.save(post);
            } else {
                throw new RuntimeException("object can not be null");
            }
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }


    @Override
    @Cacheable
    public List<Post> getAllPosts() {
        try {
            return this.repository.findAll();
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }

    @Override
    @CacheEvict
    public List<Post> getAllPostsByUserId(Integer userId) {
        try {
            return this.repository.findAllByCreatorId(userId);
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }

    @Override
    @CacheEvict
    public List<Post> getNewPosts(int limit) {
        try {
            List<Post> posts = this.repository.findByOrderByCreatedAt();
            if (posts.size() < limit)
                return this.repository.findByOrderByCreatedAt();
            return posts.subList(0, limit);
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }

    @Override
    @Cacheable
    public List<Post> getNewPosts() {
        try {
            return this.repository.findByOrderByCreatedAt();
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }

    @Override
    @SneakyThrows
    @Transactional
    public void updatePost(Post newPost, Post oldPost, MultipartFile poster) {

        Integer id = oldPost.getId();

        if (!newPost.getTitle().equals(oldPost.getTitle())) {
            this.repository.updatePostTitleById(id, newPost.getTitle());
        }
        if (!newPost.getAnons().equals(oldPost.getAnons())) {
            this.repository.updatePostAnonsById(id, newPost.getAnons());
        }
        if (!newPost.getFullText().equals(oldPost.getFullText())) {
            this.repository.updatePostFullTextById(id, newPost.getFullText());
        }

        if (!poster.isEmpty()) {
            this.repository.updatePostImageUrlById(id, new String(Base64.encodeBase64(poster.getBytes())));
        }
    }


    @Override
    public void deletePostsById(Integer id) {
        try {
            if (repository.existsById(id)) {
                this.repository.deleteById(id);
            } else {
                throw new RuntimeException("post by id = " + id + "not found and can not be deleted");
            }
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }
}
