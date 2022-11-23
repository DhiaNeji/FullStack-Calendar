package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import main.model.User;
import main.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Admin controller 
 * @author Dell
 *
 */
@Controller
public class AdminController {
    @Autowired
    private UserService userService;


    /**
     * Reload the admin panel for the admin
     */
    @GetMapping("/admin")
    public String getAdminPanel(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            userService.updateUserAttributes(userLogged, req);
        }
        return "admin";
    }

    /**
     * Manage the different users for the admin
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/admin/manageusers")
    public String getUserManagement(Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        List<User> users = userService.findAll();
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("users", users);
        }
        return "manageusers";
    }

    /**
     * Manage a user by his ID
     * @param model
     * @param authentication
     * @param id
     * @return
     */
    @GetMapping("/admin/manageusers/{id}")
    public String getUserManagement(Model model, Authentication authentication, @PathVariable Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            if (id != null) {
                User paramUser = userService.findById(id);
                model.addAttribute("paramUser", paramUser);
                model.addAttribute("currentCountry", paramUser.getCountry());
            }
        }
        return "edituser";
    }

    /**
     * Edit a user by hid ID by the admin
     * @param user
     * @param id
     * @return
     */
    @PostMapping("/admin/manageusers/{id}")
    public String editUser(User user, @PathVariable Long id) {
        User paramUser = userService.findById(id);
        userService.editByUser(paramUser, user.getFirstName(), user.getLastName(), user.getCountry(), user.getAge(),
                user.getFacebook(), user.getSkype(), user.getGithub(), user.getTwitter(), user.getEmail(), user.getUsername());
        return "redirect:/admin/manageusers/"+paramUser.getId();
    }

}
