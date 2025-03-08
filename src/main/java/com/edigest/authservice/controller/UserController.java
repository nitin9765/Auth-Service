package com.edigest.authservice.controller;

import com.edigest.authservice.entity.User;
import com.edigest.authservice.model.UserDto;
import com.edigest.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<User> createUser(UserDto user){
        return new ResponseEntity<>(userService.signUpUser(user), HttpStatus.OK);
    }
}
