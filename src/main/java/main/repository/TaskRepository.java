package main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import main.model.Task;
import main.model.User;

import java.util.List;



@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByUser(User user);
    List<Task> findByUserAndCompletedIsTrue(User user);
    List<Task> findByUserAndCompletedIsFalse(User user);
    List<Task> findByUserAndCompletedIsFalseAndApprovedIsTrue(User user);
    List<Task> findByUserAndCompletedIsTrueAndApprovedIsTrue(User user);
    List<Task> findByUserAndApprovedIsFalse(User user);
    Task findById(Long id);
    Task getById(Long id);
    void deleteById(Long id);
}
