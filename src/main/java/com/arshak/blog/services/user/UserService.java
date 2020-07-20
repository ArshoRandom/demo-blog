package com.arshak.blog.services.user;

import com.arshak.blog.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    void addUser(User user, MultipartFile file, Boolean isAdmin);

    boolean existsByEmail(String email);

    User getUserByEmail(String email);

    List<User> getAllByRoleId(Integer roleId);


    User getUserById(Integer userId);

    void deleteUserById(Integer id);
}
