package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.model.Contact;
import main.model.User;
import main.repository.ContactRepository;

import java.util.List;



@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    ContactRepository contactRepository;

    /**
     * Find a user by his details
     */
    @Override
    public List<Contact> findByUser(User user) {
        return contactRepository.findByUser(user);
    }

    /**
     * Save new user to the database
     */
    @Override
    public Contact save(Contact contact) {
        return contactRepository.save(contact);
    }

    /**
     * Delete a user by his ID
     */
    @Override
    public void deleteById(Long id) {
        contactRepository.delete(contactRepository.findById(id));
    }

    /**
     * Get a user by his ID
     */
    @Override
    public Contact getOne(Long id) {
        return contactRepository.getOne(id);
    }
}
