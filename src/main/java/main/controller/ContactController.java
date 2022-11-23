package main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.model.Contact;
import main.model.User;
import main.service.ContactService;
import main.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Contact controller
 * @author Dell
 *
 */
@Controller
public class ContactController {
    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    public User getLoggedUser(Authentication authentication) {
        return userService.findByUser(authentication.getName());
    }

    /**
     * Get the list of contacts 
     * @param req
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/contacts")
    public String contactlist(HttpServletRequest req, Model model, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Contact> contactList = contactService.findByUser(userLogged);

        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("contacts", contactList);
            userService.updateUserAttributes(userLogged, req);
        }
        return "contacts";
    }

    /**
     * Get the new contact page 
     * @param model
     * @param authentication
     * @return
     */
    @GetMapping("/contact-new")
    public String newContactPage(Model model, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        model.addAttribute("contact", new Contact());
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
        }
        return "contact-new";
    }

    /**
     * Return the new contact of a user
     * @param model
     * @param contact
     * @param authentication
     * @return
     */
    @PostMapping("/contact-new")
    public String newcontact(Model model, Contact contact, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Contact> contactList = contactService.findByUser(userLogged);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("contacts", contactList);
        }
        contactService.save(new Contact(userLogged, contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getMobileNumber(), contact.getNotes(), contact.getAddress()));
        return "redirect:/contacts";
    }

    /**
     * Delete a new contact
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/contacts-delete")
    public String deleteContact(@RequestParam Long id, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Contact> contactList = contactService.findByUser(userLogged);
        if (contactList.contains(contactService.getOne(id))) {
            contactService.deleteById(id);
            return "redirect:/contacts?deleted";
        } else {
            return "redirect:/contacts?notfound";
        }
    }

    /**
     * Edit a new contact
     * @param model
     * @param id
     * @param authentication
     * @return
     */
    @GetMapping("/contacts-edit")
    public String editContactOnScreen(Model model, @RequestParam Long id, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Contact> contactList = contactService.findByUser(userLogged);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("contacts", contactList);
        }
        if (contactList.contains(contactService.getOne(id))) {
            model.addAttribute("contact", contactService.getOne(id));
            return "/contacts-edit";
        } else {
            return "redirect:/contacts?notfound";
        }
    }

    @PostMapping("/contacts-edit")
    public String editContact(Model model, Contact contact, Authentication authentication) {
        User userLogged = getLoggedUser(authentication);
        List<Contact> contactList = contactService.findByUser(userLogged);
        if (userLogged != null) {
            model.addAttribute("loggedUser", userLogged);
            model.addAttribute("contacts", contactList);
        }
        contactService.save(new Contact(userLogged, contact.getFirstName(), contact.getLastName(), contact.getEmail(),
                contact.getMobileNumber(), contact.getNotes(), contact.getAddress()));
        contactService.deleteById(contact.getId());
        return "redirect:contacts";
    }

}
