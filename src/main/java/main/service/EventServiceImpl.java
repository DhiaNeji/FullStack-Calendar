package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.model.Event;
import main.model.User;
import main.repository.EventRepository;

import java.util.List;



@Service
public class EventServiceImpl implements EventService {
    @Autowired
    EventRepository eventRepository;

    /**
     * Find all events by A user
     */
    @Override
    public List<Event> findAllByUser(User user) {
        return eventRepository.findAllByUser(user);
    }

    /**
     * Save a new user
     */
    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    /**
     * Delete an event
     */
    @Override
    public void delete(Event event) {
        eventRepository.delete(event);
    }

    /**
     * Find an event by ID
     */
    @Override
    public Event findById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * Edit an event 
     */
    @Override
    public Event editById(Long id, String title, String start, String end) {
        Event event = eventRepository.findById(id);
        event.setStart(start);
        event.setEnd(end);
        event.setTitle(title);
        event.setDescription(event.getDescription());
        return eventRepository.save(event);
    }

    /**
     * Edit an event colour
     */
    @Override
    public Event editEventAndColour(Event event, String title, String description, String backgroundColour, String borderColour) {
        event.setTitle(title);
        event.setDescription(description);
        event.setBackgroundColor(backgroundColour);
        event.setBorderColor(borderColour);
        return eventRepository.save(event);
    }
}
