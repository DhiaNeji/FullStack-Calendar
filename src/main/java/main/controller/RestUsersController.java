package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import main.model.User;
import main.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * User controller
 * @author Dell
 *
 */
@RestController
public class RestUsersController {
    @Autowired
    private UserService userService;

    /**
     * Get the users list
     * @return
     */
    @GetMapping("/api/users")
    public List<User> getUsers() {
        List<User> users = userService.findAll();
        return users;
    }

    /**
     * Get the list of users by their usernames
     * @return
     */
    @GetMapping("/api/users/usernames")
    public List<String> getUsernames() {
        List<User> users = userService.findAll();
        ArrayList<String> usernames = new ArrayList<>();
        for (User user : users) {
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    /**
     * Get a user by his ID
     * @param id
     * @return
     */
    @GetMapping("/api/users/id/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Get the username of a user by ID
     * @param id
     * @return
     */
    @GetMapping("/api/users/id/{id}/username")
    public String getUsernameById(@PathVariable Long id) {
        return userService.findById(id).getUsername();
    }

    /**
     * Get a user by username
     * @param username
     * @return
     */
    @GetMapping("/api/users/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUser(username);
    }


   
}
