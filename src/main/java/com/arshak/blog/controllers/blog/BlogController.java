package com.arshak.blog.controllers.blog;

import com.arshak.blog.models.Post;
import com.arshak.blog.models.User;
import com.arshak.blog.services.post.PostService;
import com.arshak.blog.services.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BlogController {

    private final PostService postService;


    public BlogController(PostService postService) {
        this.postService = postService;

    }


    @GetMapping("/blog")
    public ModelAndView getPosts() {
        ModelAndView modelAndView = new ModelAndView();
        List<Post> posts = this.postService.getNewPosts();
        modelAndView.addObject("posts", posts);
        modelAndView.setViewName("blog");
        return modelAndView;
    }


    @GetMapping("/blog/{id}")
    public String getPost(@PathVariable(value = "id") Integer id, Model model) {
        Post post = this.postService.getById(id);
        List<Post> posts = this.postService.getNewPosts(6);
        posts.remove(post);
        User creator = post.getCreator();
        model.addAttribute("imageData", "data:image/png;base64,"+post.getImage());
        model.addAttribute("posts",posts);
        model.addAttribute("post", post);
        model.addAttribute("creator", creator);
        return "blog-details";
    }


}
