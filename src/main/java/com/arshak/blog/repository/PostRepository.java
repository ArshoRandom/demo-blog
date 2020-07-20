package com.arshak.blog.repository;

import com.arshak.blog.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findAllByCreatorId(Integer id);

    List<Post> findByOrderByCreatedAt();

    @Query(value = "select image_url from posts where id = ?1", nativeQuery = true)
    String findImageUrlById(Integer id);

    @Modifying
    @Query(value = "update posts set title = ?2 where id = ?1", nativeQuery = true)
    void updatePostTitleById(Integer id, String title);

    @Modifying
    @Query(value = "update posts set anons = ?2 where id = ?1", nativeQuery = true)
    void updatePostAnonsById(Integer id, String anons);

    @Modifying
    @Query(value = "update posts set full_text = ?2 where id = ?1", nativeQuery = true)
    void updatePostFullTextById(Integer id, String fullText);

    @Modifying
    @Query(value = "update posts set image = ?2 where id = ?1", nativeQuery = true)
    void updatePostImageUrlById(Integer id, String image);
}
