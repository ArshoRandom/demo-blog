package com.arshak.blog.controllers;

import com.arshak.blog.models.Post;
import com.arshak.blog.services.post.PostService;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    private final PostService service;

    public MainController(PostService service) {
        this.service = service;
    }


    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest request){
        List<Post> posts = this.service.getNewPosts(3);
        model.addAttribute("posts", posts);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()){
            for (GrantedAuthority role : auth.getAuthorities()){
                if ("USER".equals(role.getAuthority())){
                    return "redirect:/user/home";
                }else if ("ADMIN".equals(role.getAuthority())){
                    return "redirect:/admin/home";
                }
            }
        }
        return "home";
    }

    @GetMapping("/about")
    public String getAboutPage(){
        return "about";
    }

    @GetMapping("/error")
    public String getErrorPage(){
        return "error";
    }

}
