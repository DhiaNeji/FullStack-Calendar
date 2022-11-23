package main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.model.Message;
import main.model.Task;
import main.model.User;
import main.repository.TaskRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;


@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;


    @Autowired
    UserService userService;

    /**
     * Find a task by user
     */
    @Override
    public List<Task> findByUser(User user) {
        return taskRepository.findByUser(user);
    }

    /**
     * Find a task by ID
     */
    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Find a task by ID
     */
    @Override
    public Task getById(Long id) {
        return taskRepository.getById(id);
    }

    /**
     * Delete a task by ID
     */
    @Override
    public void deleteTaskById(Long id) {
        taskRepository.delete(taskRepository.findById(id));
    }


    /**
     * Change a task status to completed
     */
    @Override
    public Task completeTaskById(Long id) {
        Task task = taskRepository.findById(id);
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    /**
     * Save a new task to the database
     */
    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Get a task by his ID
     */
    @Override
    public Task getOne(Long id) {
        return taskRepository.getById(id);
    }

    /**
     * Edit a task by ID
     */
    @Override
    public Task editById(Long id, String desc, Date date, boolean completed) {
        Task task = taskRepository.getById(id);
        task.setDescription(desc);
        task.setTargetDate(date);
        task.setCompleted(task.isCompleted());
        return taskRepository.save(task);
    }

    /**
     * Find uncompleted tasks by user
     */
    @Override
    public List<Task> findByUserAndCompletedIsFalse(User user) {
        return taskRepository.findByUserAndCompletedIsFalse(user);
    }

    /**
     * Find completed tasks by user
     */
    @Override
    public List<Task> findByUserAndCompletedIsTrue(User user) {
        return taskRepository.findByUserAndCompletedIsTrue(user);
    }


    @Override
    public String getMotivationalMessage(List<Task> taskList, User user) {

        if (taskList.size() == 0 && taskRepository.findByUserAndCompletedIsTrue(user).size() == 0) {
           return "Hmm... What about giving yourself some tasks?";
        } else if (taskList.size() == 0 && taskRepository.findByUserAndCompletedIsTrue(user).size() > 0) {
            return "Good job! You've completed all your tasks! Be proud about yourself.";
        } else if (taskList.size() == 1) {
            return "What, only 1 task? You should finish your work!";
        } else if (taskList.size() > 1 && taskList.size() <= 5){
            return "You only have "+taskList.size()+" tasks... That's not that much. Go complete them!";
        } else if (taskList.size() > 5 && taskList.size() <= 10){
            return "You've got some work there, "+taskList.size()+" tasks to complete. Try to complete as much as possible today.";
        } else if (taskList.size() > 10 ){
            return taskList.size()+" tasks! Let's see how many you could complete today! Proof yourself!";
        } else {
            return "Error";
        }
    }

    /**
     * Get approved tasks and not completed by a user
     */
    @Override
    public List<Task> findByUserAndCompletedIsFalseAndApprovedIsTrue(User user) {
        return taskRepository.findByUserAndCompletedIsFalseAndApprovedIsTrue(user);
    }

    /**
     * Get approved tasks and completed by a user
     */
    @Override
    public List<Task> findByUserAndCompletedIsTrueAndApprovedIsTrue(User user) {
        return taskRepository.findByUserAndCompletedIsTrueAndApprovedIsTrue(user);
    }

    /**
     * Get unapproved tasks by a user
     */
    @Override
    public List<Task> findByUserAndApprovedIsFalse(User user) {
        return taskRepository.findByUserAndApprovedIsFalse(user);
    }


    /**
     * Approve a task to a user 
     */
    @Override
    public void approveTask(Task task) {
        task.setApproved(true);
        User user = userService.findById(task.getUser().getId());
        userService.incrementTasksReceived(user);
        User creatorUser = userService.findById(task.getCreator().getId());
        userService.incrementTasksAssigned(creatorUser);
        userService.incrementTasksCreated(user);
        taskRepository.save(task);
    }
    
    /**
     * Deny a task by a user
     */

    @Override
    public void denyTask(Task task) {
        deleteTaskById(task.getId());
        Message notifyMessage = new Message();
        notifyMessage.setReceiver(task.getCreator());
        notifyMessage.setSender(task.getUser());
        notifyMessage.setDate(new Date(Calendar.getInstance().getTime().getTime()));
        notifyMessage.setOpened(0);
        notifyMessage.setSubject("Your assigned task to "+task.getUser().getUsername()+" has been denied.");
        notifyMessage.setMessageText("<p>Hello "+task.getCreator().getUsername() + ",</p><br/>You have assigned the following task to me: <blockquote>"+task.getDescription()+
                "</blockquote><p>I hereby wish to inform you that I have denied your task.</p><small><em>This is an automated message and not written by the user self.</em></small>");
    }

    /**
     * Update informations about user tasks (count of pending tasks)
     */
    @Override
    public void updateAttributes(User user, HttpServletRequest req) {
        List<Task> taskList = taskRepository.findByUserAndCompletedIsFalseAndApprovedIsTrue(user);
        req.getSession().setAttribute("tasksLeft", taskList.size());
        String motivationMessage = getMotivationalMessage(taskList, user);
        req.getSession().setAttribute("taskMotivation", motivationMessage);
        List<Task> pendingTasks = findByUserAndApprovedIsFalse(user);
        req.getSession().setAttribute("pendingTasksCount", pendingTasks.size());
    }

 
    @Override
    public Task uncompleteTaskById(Long id) {
        Task task = taskRepository.findById(id);
        task.setCompleted(false);
        return taskRepository.save(task);
    }
}
