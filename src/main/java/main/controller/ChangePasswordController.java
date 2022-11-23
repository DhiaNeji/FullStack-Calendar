package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.model.User;
import main.repository.UserRepository;
import main.service.UserService;

import javax.servlet.http.HttpServletRequest;


/**
 * Change password controller for the user
 * @author Dell
 *
 */
@Controller
public class ChangePasswordController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Get the change password page
     * @param model
     * @param authentication
     * @param req
     * @return
     */
    @GetMapping("/changepassword")
    public String changePasswordPage(Model model, Authentication authentication, HttpServletRequest req) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            userService.updateUserAttributes(userLogged, req);
        }
        return "changepassword";
    }

    /**
     * Change the password for a user
     * @param authentication
     * @param user
     * @param comfirmPass
     * @return
     */
    @PostMapping("changepassword")
    public String updatePassword(Authentication authentication, User user, @RequestParam String comfirmPass) {
        User userLogged = userService.findByUser(authentication.getName());
        if (user.getPassword().length() >= 4 && user.getPassword().length() <= 25) {
            if (user.getPassword().equals(comfirmPass)) {
                userLogged.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(userLogged);
                return "redirect:/changepassword?success";
            } else {
                return "redirect:/changepassword?comfirm";
            }
        } else {
            return "redirect:/changepassword?error";
        }
    }
}
