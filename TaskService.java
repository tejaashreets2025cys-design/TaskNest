package com.example.taskmanager.service;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.TaskList;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.ResourceNotFoundException;
import com.example.taskmanager.repository.TaskListRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            TaskListRepository taskListRepository,
            UserRepository userRepository) {

        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
        this.userRepository = userRepository;
    }


    // =========================
    // CREATE USER
    // =========================

    public User createUser(String name, String email) {

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException(
                    "User name cannot be empty"
            );
        }

        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException(
                    "User email cannot be empty"
            );
        }

        User user = new User();

        user.setName(name.trim());
        user.setEmail(email.trim());

        return userRepository.save(user);
    }


    // =========================
    // CREATE TASK LIST
    // =========================

    public TaskList createTaskList(
            Long userId,
            String name) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        ));

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException(
                    "Task list name cannot be empty"
            );
        }

        TaskList taskList = new TaskList();

        taskList.setName(name.trim());
        taskList.setUser(user);

        return taskListRepository.save(taskList);
    }


    // =========================
    // CREATE TASK
    // =========================

    public Task createTask(
            Long taskListId,
            String title,
            LocalDate dueDate,
            String priority) {

        TaskList taskList =
                taskListRepository.findById(taskListId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Task list not found with id: "
                                                + taskListId
                                ));

        validateTaskData(
                title,
                dueDate,
                priority
        );

        Task task = new Task();

        task.setTitle(title.trim());
        task.setDueDate(dueDate);
        task.setPriority(priority.toUpperCase());
        task.setCompleted(false);
        task.setTaskList(taskList);

        return taskRepository.save(task);
    }


    // =========================
    // UPDATE TASK
    // =========================

    public Task updateTask(
            Long taskId,
            String title,
            LocalDate dueDate,
            String priority) {

        Task task = getTask(taskId);

        validateTaskData(
                title,
                dueDate,
                priority
        );

        task.setTitle(title.trim());
        task.setDueDate(dueDate);
        task.setPriority(priority.toUpperCase());

        return taskRepository.save(task);
    }


    // =========================
    // DELETE TASK
    // =========================

    public void deleteTask(Long taskId) {

        Task task = getTask(taskId);

        taskRepository.delete(task);
    }


    // =========================
    // MARK COMPLETE
    // =========================

    public Task markComplete(Long taskId) {

        Task task = getTask(taskId);

        task.setCompleted(true);

        return taskRepository.save(task);
    }


    // =========================
    // MARK INCOMPLETE
    // =========================

    public Task markIncomplete(Long taskId) {

        Task task = getTask(taskId);

        task.setCompleted(false);

        return taskRepository.save(task);
    }


    // =========================
    // TASKS DUE TODAY
    // =========================

    public List<Task> getTasksDueToday() {

        LocalDate today = LocalDate.now();

        return taskRepository.findAll()
                .stream()
                .filter(task ->
                        task.getDueDate() != null
                                && task.getDueDate().equals(today)
                )
                .toList();
    }


    // =========================
    // OVERDUE TASKS
    // =========================

    public List<Task> getOverdueTasks() {

        LocalDate today = LocalDate.now();

        return taskRepository.findAll()
                .stream()
                .filter(task ->
                        task.getDueDate() != null
                                && task.getDueDate().isBefore(today)
                                && !task.isCompleted()
                )
                .toList();
    }


    // =========================
    // MOVE TASK
    // =========================

    public Task moveTask(
            Long taskId,
            Long newTaskListId) {

        Task task = getTask(taskId);

        TaskList newTaskList =
                taskListRepository.findById(newTaskListId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Task list not found with id: "
                                                + newTaskListId
                                ));

        task.setTaskList(newTaskList);

        return taskRepository.save(task);
    }


    // =========================
    // GET ALL TASKS
    // =========================

    public List<Task> getAllTasks() {

        return taskRepository.findAll();
    }


    // =========================
    // GET TASK BY ID
    // =========================

    public Task getTask(Long taskId) {

        return taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + taskId
                        ));
    }


    // =========================
    // SEARCH TASKS
    // =========================

    public List<Task> searchTasks(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new RuntimeException(
                    "Search keyword cannot be empty"
            );
        }

        return taskRepository
                .findByTitleContainingIgnoreCase(
                        keyword.trim()
                );
    }


    // =========================
    // TASK VALIDATION
    // =========================

    private void validateTaskData(
            String title,
            LocalDate dueDate,
            String priority) {

        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException(
                    "Task title cannot be empty"
            );
        }

        if (dueDate == null) {
            throw new RuntimeException(
                    "Due date cannot be empty"
            );
        }

        if (priority == null ||
                !(priority.equalsIgnoreCase("LOW")
                || priority.equalsIgnoreCase("MEDIUM")
                || priority.equalsIgnoreCase("HIGH"))) {

            throw new RuntimeException(
                    "Invalid priority. Priority must be LOW, MEDIUM, or HIGH"
            );
        }
    }
}