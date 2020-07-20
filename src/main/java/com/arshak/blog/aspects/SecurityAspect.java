package com.arshak.blog.aspects;

import com.arshak.blog.models.User;
import com.arshak.blog.services.user.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Aspect
@Component

public class SecurityAspect {

    private final UserService userService;

    public SecurityAspect(UserService userService) {
        this.userService = userService;
    }


    @Around("execution(* com.arshak.blog.controllers.blog.BlogController.get*(..)) " +
            "|| execution(* com.arshak.blog.controllers.MainController.getAboutPage(..))")
    public Object checkAuthentication(final ProceedingJoinPoint pjp)
            throws Throwable {
        Object[] args = pjp.getArgs();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i] instanceof Model && auth.isAuthenticated()){
                for (GrantedAuthority role : auth.getAuthorities()){
                    if (role.getAuthority().equals("USER") || role.getAuthority().equals("ADMIN")){
                        Model model = (Model) args[i];
                        model.addAttribute("isAuth",auth);
                        break;
                    }
                }
            }
        }
        return pjp.proceed(args);
    }

    @Around("execution(* com.arshak.blog.controllers.user.UserController.home(..)) " +
            "|| execution(* com.arshak.blog.controllers.admin.AdminController.home(..))")
    public Object checkCurrentUserSetter(final ProceedingJoinPoint pjp) throws Throwable{
        Object[] args = pjp.getArgs();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i] instanceof Model && auth.isAuthenticated()){
                User user = userService.getUserByEmail(auth.getName());
                Model model = (Model) args[i];
                model.addAttribute("currentUser",user);
            }
        }
        return pjp.proceed(args);
    }

}
