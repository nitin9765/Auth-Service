package com.edigest.authservice.controller;

import com.edigest.authservice.entity.User;
import com.edigest.authservice.model.UserDto;
import com.edigest.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/v1")
public class AuthController {
//    "/auth/v1/login",
//            "/auth/v1/refreshToken",
//            "/auth/v1/signup"
    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody UserDto user){
        System.out.println(user.toString());
        return new ResponseEntity<>(userService.signUpUser(user), HttpStatus.OK);
    }

}
