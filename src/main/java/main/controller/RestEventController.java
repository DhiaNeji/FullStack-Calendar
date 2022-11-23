package main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import main.config.CustomAppSettings;
import main.model.Event;
import main.model.Task;
import main.model.User;
import main.service.TaskService;
import main.service.UserService;
import main.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Event Controller
 * @author Dell
 *
 */

@RestController
@RequestMapping("/api/event")
public class RestEventController {
    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    /**
     * Get the events for the authenticated user
     * @param authentication
     * @return
     */
    @GetMapping("/all")
    public String getEvents(Authentication authentication) {
        String jsonMessage = null;
        try {
            if (authentication != null) {
                User userLogged = userService.findByUser(authentication.getName());
                List<Event> events = eventService.findAllByUser(userLogged);
                List<Task> tasks = taskService.findByUserAndCompletedIsFalseAndApprovedIsTrue(userLogged);
                if (userLogged.isTodoToCalendar()) {
                    for (Task task : tasks) {
                        events.add(new Event("To-do: "+task.getDescription(), "Task from: " + task.getCreator().getFirstName() + " " + task.getCreator().getLastName(), task.getTargetDate().toString(), task.getTargetDate().toString(), userLogged, CustomAppSettings.EVENT_TODO_COLOUR, CustomAppSettings.EVENT_TODO_COLOUR, false));
                    }
                }
                ObjectMapper mapper = new ObjectMapper();
                jsonMessage = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
            } else {
                return "You are not authenticated";
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return jsonMessage;
    }
}
