package main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.model.Contact;
import main.model.User;

import java.util.List;



@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>{
    List<Contact> findByUser(User user);
    Contact findById(Long id);
}
