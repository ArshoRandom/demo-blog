package com.arshak.blog.controllers.user;

import com.arshak.blog.models.Post;
import com.arshak.blog.models.User;
import com.arshak.blog.services.post.PostService;
import com.arshak.blog.services.user.UserService;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
@RequestMapping("/user/")
public class UserController {

    private final PostService postService;
    private final UserService userService;
    private Map<String, User> currentUser = new ConcurrentHashMap<>();


    public UserController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }


    @GetMapping(value = "home")
    public String home(Model model) {
        User user = (User) model.getAttribute("currentUser");
        List<Post> userPosts = this.postService.getAllPostsByUserId(user.getId());
        model.addAttribute("userPosts", userPosts);
        return "user/home";
    }

    @GetMapping("blog/add")
    public String addPost() {
        return "user/blog-add";
    }

    @GetMapping("blog/{id}/edit")
    public String editPost(@PathVariable(value = "id") Integer id, Model model) {
        Post post = this.postService.getById(id);
        model.addAttribute("post", post);
        return "user/blog-edit";
    }

    @SneakyThrows
    @PostMapping(value = "blog/{id}/edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updatePost(@PathVariable("id") Integer id,
                             @RequestParam String title,
                             @RequestParam String anons,
                             @RequestParam String fullText,
                             @RequestParam("poster") MultipartFile poster) {
        Post oldPost = this.postService.getById(id);
        Post newPost = new Post();
        newPost.setTitle(title);
        newPost.setAnons(anons);
        newPost.setFullText(fullText);
        this.postService.updatePost(newPost, oldPost, poster);
        return "redirect:/user/home";
    }


    @SneakyThrows
    @PostMapping(value = "blog/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createPost(@RequestParam String title,
                             @RequestParam String anons,
                             @RequestParam String fullText,
                             @RequestParam("poster") MultipartFile poster) {
        Post post = new Post();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFullText(fullText);
        post.setCreator(this.getCurrentUser());
        post.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        this.postService.addPost(post, poster);
        return "redirect:/user/home";
    }


    @PostMapping("blog/{id}/delete")
    @DeleteMapping("/post/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Integer id) {
        this.postService.deletePostsById(id);
        return "redirect:/user/home";
    }

    private User getCurrentUser() {
        if (this.currentUser.isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.getUserByEmail(auth.getName());
            currentUser.put("user", user);
            return user;
        } else {
            return currentUser.get("user");
        }
    }
}
