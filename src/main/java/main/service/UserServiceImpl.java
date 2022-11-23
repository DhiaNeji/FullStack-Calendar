package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import main.web.dto.UserRegistrationDto;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskService taskService;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Register new user to the database
     */
    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setRegistrationDate(new Date(Calendar.getInstance().getTime().getTime()));
        return userRepository.save(user);
    }

    /**
     * Find a user by his details
     */
    @Override
    public User findByUser(String user) {
        return userRepository.findByUsername(user);
    }

    /**
     * find a user by his ID
     */
    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * find the list of all users
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Create a new bug report
     */
    @Override
    public void addBugReport(User userLogged) {
        userLogged.setBugsReported(userLogged.getBugsReported()+1);
        userRepository.save(userLogged);
    }

    /**
     * Edit a user details
     */
    @Override
    public User editByUser(User user, String firstName, String lastName, String country, int age, String facebook, String skype, String github, String twitter, String email, String username) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCountry(country);
        user.setAge(age);
        user.setFacebook(facebook);
        user.setSkype(skype);
        user.setGithub(github);
        user.setTwitter(twitter);
        user.setEmail(email);
        user.setUsername(username);
        return userRepository.save(user);
    }

    /**
     * Increment messagesReceivedStats from {@link User}
     * @param user User
     */
    @Override
    public void incrementMessagesReceivedStats(User user) {
        user.setMessagesReceived(user.getMessagesReceived()+1);
        userRepository.save(user);
    }

    /**
     * Increment incrementMessagesSentStats from {@link User}
     * @param user User
     */
    @Override
    public void incrementMessagesSentStats(User user) {
        user.setMessagesSent(user.getMessagesSent()+1);
        userRepository.save(user);
    }

    /**
     * Set social details of a user
     */
    @Override
    public User setSocialSettings(User user, String facebook, String twitter, String skype, String github) {
        user.setFacebook(facebook);
        user.setTwitter(twitter);
        user.setSkype(skype);
        user.setGithub(github);
        return userRepository.save(user);
    }

    /**
     * Set the name of a user
     */
    @Override
    public User setName(User user, String firstName, String lastName) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return userRepository.save(user);
    }

    /**
     * Increment the number of tasks created for a user 
     */
    @Override
    public void incrementTasksCreated(User user) {
        user.setTasksMade(user.getTasksMade()+1);
        userRepository.save(user);
    }

    /**
     * Decrement the tasks created for the user
     */
    @Override
    public void decrementTasksCreated(User user) {
        user.setTasksMade(user.getTasksMade()-1);
        userRepository.save(user);
    }

    /**
     * Increment the received tasks for a user
     */
    @Override
    public void incrementTasksReceived(User user) {
        user.setTasksReceived(user.getTasksReceived()+1);
        userRepository.save(user);
    }

    /**
     * Increment the assigned tasks for a user
     */
    @Override
    public void incrementTasksAssigned(User user) {
        user.setTasksAssigned(user.getTasksAssigned()+1);
        userRepository.save(user);
    }

    /**
     * Update the user attributes
     */
    @Override
    public void updateUserAttributes(User user, HttpServletRequest req) {
        taskService.updateAttributes(user, req);
    }

    /**
     * increment the number of completed tasks for a user
     */
    @Override
    public void incrementTasksCompleted(User user) {
        user.setTasksCompleted(user.getTasksCompleted()+1);
        userRepository.save(user);
    }

    /**
     * Decrement the number of completed tasks for a user
     */
    @Override
    public void decrementTasksCompleted(User user) {
        user.setTasksCompleted(user.getTasksCompleted()-1);
        userRepository.save(user);
    }

    @Override
    public User setMotivationalTaskMessage(User user, boolean value) {
        user.setMotivationalTaskMessage(value);
        return userRepository.save(user);
    }

    /**
     * Set the small calendar for a user
     */
    @Override
    public User setSmallCalendar(User user, boolean value) {
        user.setSmallCalendar(value);
        return userRepository.save(user);
    }

    /**
     * Set the todo's in calendar for a user
     */
    @Override
    public User setTodoToCalendar(User user, boolean value) {
        user.setTodoToCalendar(value);
        return userRepository.save(user);
    }

    /**
     * Set the show email attribute to true or false
     */
    @Override
    public User setShowEmail(User user, boolean value) {
        user.setShowEmail(value);
        return userRepository.save(user);
    }

    /**
     * Method to tell spring security the logic to use when login
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * Configure the roles of a user
     * 
     */
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
