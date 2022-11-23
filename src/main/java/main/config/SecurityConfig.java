package main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import main.service.UserService;

/**
 * Security configuration class 
 * @author Dell
 *
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    
    /**
     * Tell spring security the list of URL that are protected
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/api/**",
                            "/webjars/**").permitAll()
                    .antMatchers("/calendar", "/calendar-delete", "/calendar-edit", "/calendar-addEvent", "/calendar-updateEvent",
                            "/contacts","/contact-edit",
                            "/", "/index",
                            "/budget", "/budget-new", "/budget-list",
                            "expense-edit", "income-edit",
                            "/task-list", "/task-assign","/tasks-pending", "/task-approve", "/task-deny",
                            "/profile", "/upload-avatar",
                            "/users",
                            "/settings","/appsettings","/changepassword",
                            "/messages", "/message", "/message-new","/message-to",
                            "/report-bug").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_TESTER")
                    .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .and()
                    .formLogin()
                        .loginPage("/login")
                            .permitAll()
                .and()
                    .logout()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout")
                .permitAll();
    }

    /**
     * Define the encryption algorithm
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Define the Auth provider for the application
     * @return
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    /**
     * Configure the Auth Manager for the application
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
}