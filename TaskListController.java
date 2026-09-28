package com.example.taskmanager.controller;

import com.example.taskmanager.entity.TaskList;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task-lists")
@Validated
public class TaskListController {

    private final TaskService taskService;

    public TaskListController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create task list
    @PostMapping
    public ResponseEntity<TaskList> createTaskList(

            @RequestParam
            @Positive(message = "User ID must be a positive number")
            Long userId,

            @RequestParam
            @NotBlank(message = "Task list name cannot be empty")
            String name) {

        TaskList taskList =
                taskService.createTaskList(
                        userId,
                        name
                );

        return new ResponseEntity<>(
                taskList,
                HttpStatus.CREATED
        );
    }
}