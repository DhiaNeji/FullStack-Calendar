package main.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
/**
 * Login controller
 * @author Dell
 *
 */
@Controller
public class LoginController {
	/**
	 * Check if the user is connected else redirect to the login page
	 * @param model
	 * @param authentication
	 * @return
	 */
    @GetMapping("/login")
    public String login(Model model, Authentication authentication) {
        if (authentication == null) {
            model.addAttribute("page-title", "Login");
            return "login";
        } else {
            return "redirect:/";
        }

    }
}
