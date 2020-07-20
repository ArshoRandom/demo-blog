package com.arshak.blog.repository;

import com.arshak.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    User findUserByEmail(String email);

    @Query(value = "select * from users u inner join user_roles ur on(u.id=ur.user_id) inner join roles r on ur.role_id = r.id where role_id = ?1",
    nativeQuery = true)
    List<User> findAllByRoleId(Integer roleId);

    @Query(value = "select image_url from users where id = ?1", nativeQuery = true)
    String findImageUrlById(Integer id);
}
