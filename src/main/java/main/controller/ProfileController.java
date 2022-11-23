package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.model.User;
import main.service.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Profile controller
 * @author Dell
 *
 */

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    /**
     * Redirect the user to his profile
     * @param req
     * @param model
     * @param authentication
     * @param id
     * @return
     */
    @GetMapping("/profile")
    public String root(HttpServletRequest req, Model model, Authentication authentication, @RequestParam(required = false) Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        model.addAttribute("loggedUser", userLogged);
        if (userLogged != null) {
            userService.updateUserAttributes(userLogged, req);
        }
        if (id != null) {
            User paramUser = userService.findById(id);
            if (paramUser != null) {
                model.addAttribute("paramUser", paramUser);

            } else {
                return "redirect:/profile?id="+userLogged.getId();
            }
        } else {
            return "redirect:/profile?id="+userLogged.getId();
        }
        return "profile";
    }
}
