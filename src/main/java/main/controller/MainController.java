package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import main.model.User;
import main.service.UserService;
import main.utility.CustomTimeMessage;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    /**
     * Check if the user is connected and redirects him to the index page
     * @param req
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/")
    public String root(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());

        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            CustomTimeMessage customTimeMessage = new CustomTimeMessage();
            model.addAttribute("timeGreeting", customTimeMessage.getMessage());

            userService.updateUserAttributes(userLogged, req);
        }

        return "index";
    }
}
