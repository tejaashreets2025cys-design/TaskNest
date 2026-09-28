package com.example.taskmanager.controller;

import com.example.taskmanager.entity.User;
import com.example.taskmanager.service.TaskService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final TaskService taskService;

    public UserController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create user
    @PostMapping
    public ResponseEntity<User> createUser(

            @RequestParam
            @NotBlank(message = "User name cannot be empty")
            String name,

            @RequestParam
            @NotBlank(message = "User email cannot be empty")
            @Email(message = "Invalid email format")
            String email) {

        User user = taskService.createUser(name, email);

        return new ResponseEntity<>(
                user,
                HttpStatus.CREATED
        );
    }
}