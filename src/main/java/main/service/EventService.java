package main.service;

import java.util.List;

import main.model.Event;
import main.model.User;


public interface EventService {
    List<Event> findAllByUser(User user);
    Event save(Event event);
    void delete(Event event);
    Event findById(Long id);
    Event editById(Long id, String title, String start, String end);
    Event editEventAndColour(Event event, String title, String description, String backgroundColour, String borderColour);
}
