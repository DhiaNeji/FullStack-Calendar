package main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.Event;
import main.model.User;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);
    Event findById(Long id);
}
