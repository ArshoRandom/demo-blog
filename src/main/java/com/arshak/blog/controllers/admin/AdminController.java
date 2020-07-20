package com.arshak.blog.controllers.admin;

import com.arshak.blog.models.Post;
import com.arshak.blog.models.User;
import com.arshak.blog.services.post.PostService;
import com.arshak.blog.services.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final UserService userService;
    private final PostService postService;

    public AdminController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }


    @GetMapping(value = "home")
    public String home(Model model) {
        User user = (User) model.getAttribute("currentUser");
        List<User> allUsers = this.userService.getAllByRoleId(2);
        model.addAttribute("users",allUsers);
        model.addAttribute("userName", "welcome to admin panel " + user.getName() + " " + user.getSurname() + " (" + user.getEmail() + ")");
        return "admin/home";
    }

    @GetMapping(value = "user/{userId}")
    public ModelAndView getUserPosts(@PathVariable("userId") Integer userId){
        ModelAndView modelAndView = new ModelAndView();
        List<Post> userPosts = this.postService.getAllPostsByUserId(userId);
        modelAndView.addObject("userPosts", userPosts);
        modelAndView.setViewName("admin/user-data");
        return modelAndView;
    }

    @PostMapping("user/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Integer id) {
        this.userService.deleteUserById(id);
        return "redirect:/admin/home";
    }

    @PostMapping("blog/{id}/delete")
    public String deletePost(@PathVariable(value = "id") Integer id) {
        this.postService.deletePostsById(id);
        return "redirect:/admin/home";
    }

}
