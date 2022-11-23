package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.model.Event;
import main.model.User;
import main.service.UserService;
import main.service.EventService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class CalendarController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @GetMapping("/calendar")
    public String calendar(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = userService.findByUser(authentication.getName());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("event", new Event());
            userService.updateUserAttributes(userLogged, req);
        }
        return "calendar";
    }

    @GetMapping("/calendar-addevent")
    public String calendarAddEvent(Authentication authentication, @RequestParam String start, @RequestParam String end, @RequestParam String title, @RequestParam String description, @RequestParam String colour) {
        User userLogged = userService.findByUser(authentication.getName());

        if (userLogged != null) {
            if (title.length() > 0) {
                eventService.save(new Event(title, description, start, end, userLogged, colour, colour, true));
                return "redirect:/calendar";
            } else {
                return "redirect:/calendar?error";
            }
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/calendar-delete")
    public String deleteEvent(Authentication authentication, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        eventService.delete(eventService.findById(id));
        return "redirect:/calendar";
    }

    @GetMapping("/calendar-update")
    public String updateEvent(Authentication authentication, @RequestParam String title, @RequestParam String start, @RequestParam String end, @RequestParam Long id) {
        User userLogged = userService.findByUser(authentication.getName());
        eventService.editById(id, title, start, end);
        return "redirect:/calendar";
    }

    @GetMapping("/calendar-updateevent")
    public String updateEventNew(Authentication authentication, @RequestParam String title, @RequestParam String start, @RequestParam String end, @RequestParam Long id, @RequestParam String colour, @RequestParam String desc) {
        User userLogged = userService.findByUser(authentication.getName());
        Event event = eventService.findById(id);
        if (title.length() > 0) {
            eventService.editEventAndColour(event, title, desc, colour,  colour);
        } else {
            eventService.editEventAndColour(event, "Event", desc, colour,  colour);
        }
        return "redirect:/calendar";
    }
}
