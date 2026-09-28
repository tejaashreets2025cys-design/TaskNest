package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@Validated
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    // =========================================================
    // GET ALL TASKS
    // =========================================================

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {

        return ResponseEntity.ok(
                taskService.getAllTasks()
        );
    }


    // =========================================================
    // CREATE TASK
    // =========================================================

    @PostMapping
    public ResponseEntity<Task> createTask(

            @RequestParam
            @Positive(
                    message = "Task list ID must be a positive number"
            )
            Long taskListId,

            @RequestParam
            @NotBlank(
                    message = "Task title cannot be empty"
            )
            String title,

            @RequestParam
            @NotBlank(
                    message = "Due date cannot be empty"
            )
            String dueDate,

            @RequestParam
            @NotBlank(
                    message = "Priority cannot be empty"
            )
            String priority) {

        Task task = taskService.createTask(
                taskListId,
                title,
                LocalDate.parse(dueDate),
                priority
        );

        return new ResponseEntity<>(
                task,
                HttpStatus.CREATED
        );
    }


    // =========================================================
    // GET TASK BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(

            @PathVariable
            @Positive(
                    message = "Task ID must be a positive number"
            )
            Long id) {

        return ResponseEntity.ok(
                taskService.getTask(id)
        );
    }


    // =========================================================
    // UPDATE TASK
    // =========================================================

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(

            @PathVariable
            @Positive(
                    message = "Task ID must be a positive number"
            )
            Long id,

            @RequestParam
            @NotBlank(
                    message = "Task title cannot be empty"
            )
            String title,

            @RequestParam
            @NotBlank(
                    message = "Due date cannot be empty"
            )
            String dueDate,

            @RequestParam
            @NotBlank(
                    message = "Priority cannot be empty"
            )
            String priority) {

        Task task = taskService.updateTask(
                id,
                title,
                LocalDate.parse(dueDate),
                priority
        );

        return ResponseEntity.ok(task);
    }


    // =========================================================
    // DELETE TASK
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(

            @PathVariable
            @Positive(
                    message = "Task ID must be a positive number"
            )
            Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }


    // =========================================================
    // MARK TASK COMPLETE
    // =========================================================

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markComplete(

            @PathVariable
            @Positive(
                    message = "Task ID must be a positive number"
            )
            Long id) {

        return ResponseEntity.ok(
                taskService.markComplete(id)
        );
    }


    // =========================================================
    // MARK TASK INCOMPLETE
    // =========================================================

    @PutMapping("/{id}/incomplete")
    public ResponseEntity<Task> markIncomplete(

            @PathVariable
            @Positive(
                    message = "Task ID must be a positive number"
            )
            Long id) {

        return ResponseEntity.ok(
                taskService.markIncomplete(id)
        );
    }


    // =========================================================
    // GET TASKS DUE TODAY
    // =========================================================

    @GetMapping("/today")
    public ResponseEntity<List<Task>> getTasksDueToday() {

        return ResponseEntity.ok(
                taskService.getTasksDueToday()
        );
    }


    // =========================================================
    // GET OVERDUE TASKS
    // =========================================================

    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {

        return ResponseEntity.ok(
                taskService.getOverdueTasks()
        );
    }


    // =========================================================
    // SEARCH TASKS
    // =========================================================

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(

            @RequestParam
            @NotBlank(
                    message = "Search keyword cannot be empty"
            )
            String keyword) {

        return ResponseEntity.ok(
                taskService.searchTasks(keyword)
        );
    }


    // =========================================================
    // MOVE TASK TO ANOTHER LIST
    // =========================================================

    @PutMapping("/{id}/move")
    public ResponseEntity<Task> moveTask(

            @PathVariable
            @Positive(
                    message = "Task ID must be a positive number"
            )
            Long id,

            @RequestParam
            @Positive(
                    message = "New task list ID must be a positive number"
            )
            Long newTaskListId) {

        return ResponseEntity.ok(
                taskService.moveTask(
                        id,
                        newTaskListId
                )
        );
    }
}