package com.arshak.blog.controllers.auth;

import com.arshak.blog.models.User;
import com.arshak.blog.services.user.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;


@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("auth/register");
        return modelAndView;
    }


    @PostMapping(value = "/register" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult,
                                      @RequestParam("avatar") MultipartFile file,
                                      @RequestParam(value = "isAdmin", required = false) boolean isAdmin ) {
        ModelAndView modelAndView = new ModelAndView();

        if (this.userService.existsByEmail(user.getEmail())) {
            bindingResult
                    .rejectValue("email", "error.userEntity",
                            "There is already a user registered with the email provided");
        }
        if (!bindingResult.hasErrors()) {
            userService.addUser(user,file,isAdmin);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("auth/login");
        }else {
            modelAndView.setViewName("auth/register");
        }

        return modelAndView;
    }

}
