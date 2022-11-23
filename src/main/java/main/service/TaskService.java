package main.service;

import javax.servlet.http.HttpServletRequest;

import main.model.Task;
import main.model.User;

import java.sql.Date;
import java.util.List;


public interface TaskService {
    List<Task> findByUser(User user);
    Task findById(Long id);
    Task getById(Long id);
    void deleteTaskById(Long id);
    Task completeTaskById(Long id);
    Task uncompleteTaskById(Long id);
    Task save(Task task);
    Task getOne(Long id);
    Task editById(Long id, String desc, Date date, boolean completed);
    List<Task> findByUserAndCompletedIsFalse(User user);
    List<Task> findByUserAndCompletedIsTrue(User user);
    String getMotivationalMessage(List<Task> taskList, User user);
    List<Task> findByUserAndCompletedIsFalseAndApprovedIsTrue(User user);
    List<Task> findByUserAndCompletedIsTrueAndApprovedIsTrue(User user);
    List<Task> findByUserAndApprovedIsFalse(User user);
    void approveTask(Task task);
    void denyTask(Task task);
    void updateAttributes(User user, HttpServletRequest req);
}
