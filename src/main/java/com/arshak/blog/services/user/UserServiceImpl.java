package com.arshak.blog.services.user;

import com.arshak.blog.exceptions.ServiceLayerException;
import com.arshak.blog.models.Role;
import com.arshak.blog.models.User;
import com.arshak.blog.repository.RoleRepository;
import com.arshak.blog.repository.UserRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void addUser(User user, MultipartFile file, Boolean isAdmin) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role userRole = isAdmin ? roleRepository.findByRole("ADMIN") : roleRepository.findByRole("USER");
            user.setActive(1);
            user.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            if (!file.isEmpty()) {
                user.setImage(new String(Base64.encodeBase64(file.getBytes())));
            }else {
                user.setImage(new String(Base64.encodeBase64(Files.readAllBytes(Paths.get("src/main/resources/static/images/user/default.png")))));
            }
            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return this.userRepository.existsByEmail(email);
        } catch (Exception e) {
            throw new ServiceLayerException(e);
        }
    }

    @Override
    @Cacheable(key = "#email")
    public User getUserByEmail(String email) {
        try {
            return this.userRepository.findUserByEmail(email);
        }catch (Exception e){
            throw new ServiceLayerException(e);
        }
    }

    @Override
    @CacheEvict
    public List<User> getAllByRoleId(Integer roleId) {
        try {
            return this.userRepository.findAllByRoleId(roleId);
        }catch (Exception e){
            throw new ServiceLayerException(e);
        }
    }

    @Override
    public User getUserById(Integer userId) {
        try {
            Optional<User> optionalUser = this.userRepository.findById(userId);
            if (optionalUser.isPresent()){
                return optionalUser.get();
            }
            return null;

        }catch (Exception e){
            throw new ServiceLayerException(e);
        }

    }

    @Override
    public void deleteUserById(Integer id) {
        try {
            this.userRepository.deleteById(id);
        }catch (Exception e){
            throw new ServiceLayerException(e);
        }
    }
}
