package com.smartpeso.controllers;

import com.smartpeso.model.User;
import com.smartpeso.model.dto.UserCreationDTO;
import com.smartpeso.services.UserService;
import com.smartpeso.validators.UserValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreationDTO userCreationDto) {
        try {
            User user = userService.createUser(userCreationDto.email(), userCreationDto.firstName(), userCreationDto.lastName());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch(UserValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
