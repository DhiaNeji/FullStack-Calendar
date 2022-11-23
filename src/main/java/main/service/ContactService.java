package main.service;

import java.util.List;

import main.model.Contact;
import main.model.User;


public interface ContactService {
    List<Contact> findByUser(User user);
    Contact save(Contact contact);
    void deleteById(Long id);
    Contact getOne(Long id);
}
