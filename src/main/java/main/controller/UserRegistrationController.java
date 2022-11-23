package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.model.User;
import main.service.UserService;
import main.web.dto.UserRegistrationDto;

import javax.validation.Valid;

/**
 * Registration controller
 * @author Dell
 *
 */

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
/**
 * Check if the user is connected, else redirect to registration page
 * @param authentication
 * @return
 */
    @GetMapping
    public String showRegistrationForm(Authentication authentication) {
        if (authentication == null) {
            return "registration";
        } else {
            return "redirect:/";
        }
    }

    /**
     * Create a new account of a new user
     * @param userDto
     * @param result
     * @return
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User existing_username = userService.findByUser(userDto.getUsername());
        if (existing_username != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

        User existing_email = userService.findByEmail(userDto.getEmail());
        if (existing_email != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (result.hasErrors()) {
            return "registration";
        }
        userService.save(userDto);
        return "redirect:/registration?success";
    }

}
