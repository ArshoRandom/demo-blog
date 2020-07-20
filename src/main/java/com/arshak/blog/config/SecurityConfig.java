package com.arshak.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final PasswordEncoder passwordEncoder;

    private final DataSource dataSource;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(usersQuery)
                .authoritiesByUsernameQuery(rolesQuery)
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/about").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/blog/**").permitAll()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/user/**").hasAuthority("USER")
                    .antMatchers("/admin/**").hasAuthority("ADMIN")
                .anyRequest().anonymous()
                .and().csrf().disable()
                    .formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                .successHandler(new AuthenticationSuccessHandler() {
                    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse,
                                                        Authentication authentication) throws IOException, ServletException {
                        boolean isAdmin = false;
                        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                            if ("ADMIN".equals(grantedAuthority.getAuthority())) {
                                isAdmin = true;
                                break;
                            }
                        }
                        if (isAdmin) {
                            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/admin/home");
                        } else {
                            redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/user/home");
                        }
                    }
                })
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and().exceptionHandling()
                .accessDeniedPage("/error");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**","/fonts/**");
    }

}
